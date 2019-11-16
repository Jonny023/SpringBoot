package com.example.entity;

import java.io.Serializable;

/**
 * @author Jonny
 * @description
 * @date 2019/11/16 0016
 */
public class User implements Serializable {

    private Long id;
    private String username;
    private String nickname;
    private String password;

    public User() {}

    public User(Long id, String username, String nickname, String password) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
