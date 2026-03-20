package com.hcltech.sample.redis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {
    @JsonIgnore
    private String id;
    private String name;
    private String email;
    private String city;
    private String country;
}
