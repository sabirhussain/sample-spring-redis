package com.hcltech.sample.redis.resource;

import com.hcltech.sample.redis.entity.User;
import com.hcltech.sample.redis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String id = userService.createUser(user);
        return ResponseEntity.ok(userService.getUser(id));
    }
}
