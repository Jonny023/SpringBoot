package com.example.springbootlogin.constant;

public interface Const {

    String JWT_SECRET_KEY = "aBCDEFG1234567890..";

    /**
     * token有效期2分钟，测试过期
     */
    long EXPIRATION_TIME = 2 * 60 * 1000;
}