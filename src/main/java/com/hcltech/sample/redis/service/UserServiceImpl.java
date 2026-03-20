package com.hcltech.sample.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.sample.redis.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }

        try {
            user.setId(UUID.randomUUID().toString());
            String userAsJson = objectMapper.writeValueAsString(user);
            stringRedisTemplate.opsForValue().set(user.getId(), userAsJson);
            //log.info("user has been created: {}", userAsJson);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

        return user.getId();
    }

    @Override
    public User getUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        String userAsJson = stringRedisTemplate.opsForValue().get(id);
        if (userAsJson == null) {
            throw new RuntimeException(String.format("user does not exist: %s", id));
        }

        try {
            return objectMapper.readValue(userAsJson, User.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
