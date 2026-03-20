package com.hcltech.sample.redis.service;

import com.hcltech.sample.redis.entity.User;

public interface UserService {
    String createUser(User user);

    User getUser(String id);
}
