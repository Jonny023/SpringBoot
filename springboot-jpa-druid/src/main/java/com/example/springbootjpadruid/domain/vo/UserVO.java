package com.example.springbootjpadruid.domain.vo;

import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String username;

    private Integer age;

    private String address;

    private String userRole;
}
