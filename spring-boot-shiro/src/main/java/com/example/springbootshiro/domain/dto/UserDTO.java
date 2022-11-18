package com.example.springbootshiro.domain.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String roles;
    private String rights;
}
