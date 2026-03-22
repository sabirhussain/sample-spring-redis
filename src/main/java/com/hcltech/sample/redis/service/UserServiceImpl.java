package com.hcltech.sample.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.sample.redis.entity.User;
import glide.api.GlideClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService {
    private final GlideClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }

        try {
            user.setId(UUID.randomUUID().toString());
            String userAsJson = objectMapper.writeValueAsString(user);
            String result = client.set(user.getId(), userAsJson).get(1, TimeUnit.SECONDS);
            //log.info("user has been created: {}", result);
        } catch (JsonProcessingException | InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }

        return user.getId();
    }

    @Override
    public User getUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        try {
            String userAsJson = client.get(id).get(1, TimeUnit.SECONDS);
            if (userAsJson == null) {
                throw new RuntimeException(String.format("user does not exist: %s", id));
            }
            return objectMapper.readValue(userAsJson, User.class);
        } catch (JsonProcessingException | InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }
    }
}
