## Code Review — UserServiceImpl.java

**Prompt**
```aiignore
review src/main/java/com/hcltech/sample/redis/service/UserServiceImpl.java
```

**Project:** Java 17 · Spring Boot 3.5.11 · `com.hcltech.sample.redis`

---

### 🔴 Critical — Functional Bug (Silent Data Loss)

**`User.id` is `@JsonIgnore` — `getUser` always returns a `User` with `id == null`**

`@JsonIgnore` on `User.id` excludes the field from *both* serialization and deserialization. The id is stored only as the Redis key — never in the JSON value — so it is never restored when reading back.

```java
// createUser: id assigned, then silently dropped from JSON
user.setId(UUID.randomUUID().toString());
String userAsJson = objectMapper.writeValueAsString(user); // id NOT in the JSON

// getUser: deserialized User always has id == null
return objectMapper.readValue(userAsJson, User.class);     // getId() == null
```

**Fix — restore the id from the Redis key after deserialization:**
```java
User fetched = objectMapper.readValue(userAsJson, User.class);
fetched.setId(id);
return fetched;
```

---

### 🟠 SOLID Violation — Dependency Inversion Principle

**`ObjectMapper` constructed inline, bypassing Spring DI**

```java
// VIOLATION — initialised inline, Lombok skips this in the constructor
private final ObjectMapper objectMapper = new ObjectMapper();
```

`@RequiredArgsConstructor` only injects *uninitialized* `final` fields. This instance is completely isolated from Spring Boot's auto-configured `ObjectMapper` bean, which may carry `JavaTimeModule`, custom serializers, or `MapperFeature` settings defined elsewhere.

**Fix — remove the inline initializer:**
```java
private final StringRedisTemplate stringRedisTemplate;
private final ObjectMapper objectMapper; // Spring injects the configured bean
```

---

### 🟡 DRY Violation — Duplicated `JsonProcessingException` Wrapping

Identical `try/catch` wrapping `JsonProcessingException → RuntimeException` appears in both methods.

**Fix — extract private helpers:**
```java
private String toJson(Object value) {
    try { return objectMapper.writeValueAsString(value); }
    catch (JsonProcessingException ex) { throw new RuntimeException(ex); }
}

private <T> T fromJson(String json, Class<T> type) {
    try { return objectMapper.readValue(json, type); }
    catch (JsonProcessingException ex) { throw new RuntimeException(ex); }
}
```

---

### 🔵 Testability Issue

The inline `new ObjectMapper()` makes it impossible to substitute a controlled mapper in unit tests. Fixed automatically by the DIP fix above — the mapper becomes a constructor-injected dependency that can be mocked with Mockito.

---

### 🔵 Maintainability Issues

**1. Raw `RuntimeException` for a business condition**

```java
throw new RuntimeException(String.format("user does not exist: %s", id));
```

Controllers and tests cannot distinguish a not-found condition from a technical failure without parsing the exception message. Introduce a typed domain exception:
```java
throw new UserNotFoundException(id);
```

**2. PII logged at INFO level**

```java
log.info("user has been created: {}", userAsJson); // logs email, city, country
```

Log only the id to avoid exposing personal data in log aggregators:
```java
log.info("user has been created: id={}", user.getId());
```

---

### ✅ Summary

| # | Finding | Severity |
|---|---|---|
| 1 | `getUser` returns `User` with `id == null` due to `@JsonIgnore` on `User.id` | 🔴 Critical |
| 2 | `ObjectMapper` initialized inline, bypasses Spring DI / auto-configuration | 🟠 SOLID |
| 3 | `JsonProcessingException` catch block duplicated across two methods | 🟡 DRY |
| 4 | Inline `ObjectMapper` not injectable / mockable in unit tests | 🔵 Testability |
| 5 | Raw `RuntimeException` for not-found; PII fields in logs | 🔵 Maintainability |

**Overall verdict: `NEEDS WORK`**

The `@JsonIgnore` + `getUser` data-loss bug is the must-fix before this code ships. The DIP violation is a close second — it silently overrides any Spring Boot `ObjectMapper` configuration across the application.