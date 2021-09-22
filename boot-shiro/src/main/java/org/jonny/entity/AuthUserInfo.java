package org.jonny.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthUserInfo implements Serializable {
    private Long id;
    private String username;
    private String avatar;
}