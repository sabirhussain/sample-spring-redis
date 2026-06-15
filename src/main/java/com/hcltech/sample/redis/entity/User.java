package com.hcltech.sample.redis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record User(
        @JsonIgnore
        String id,
        String name,
        String email,
        String city,
        String country
) {
    public User withId(String id) {
        return new User(id, name, email, city, country);
    }
}
