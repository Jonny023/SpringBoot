package com.example.springbootdemo.domain.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private String username;
    private String sex;
    private Integer age;
}
