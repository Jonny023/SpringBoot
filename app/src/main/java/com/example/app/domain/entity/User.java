package com.example.app.domain.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private Long id;
    private String username;
}