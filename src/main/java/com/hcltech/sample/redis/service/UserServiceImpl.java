package com.hcltech.sample.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.sample.redis.entity.User;
import com.hcltech.sample.redis.exception.ResourceNotFoundException;
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
    private final ObjectMapper objectMapper;

    @Override
    public String createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }

        try {
            String id = UUID.randomUUID().toString();
            String userAsJson = objectMapper.writeValueAsString(user);
            stringRedisTemplate.opsForValue().set(id, userAsJson);
            log.info("user has been created: id={}", id);
            return id;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public User getUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        String userAsJson = stringRedisTemplate.opsForValue().get(id);
        if (userAsJson == null) {
            throw new ResourceNotFoundException(User.class.getCanonicalName(), id);
        }

        try {
            User user = objectMapper.readValue(userAsJson, User.class);
            return user.withId(id);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
