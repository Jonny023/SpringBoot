package com.example.security.enums;

import com.example.security.handler.impl.EmailPasswordAuthHandler;
import com.example.security.handler.impl.PhoneCodeAuthHandler;
import com.example.security.handler.impl.UsernamePasswordAuthHandler;
import lombok.Getter;

/**
 * 登录类型
 *
 * @author Jonny
 */
@Getter
public enum LoginEnum {

    /**
     * 用户名+密码
     */
    USERNAME_PASSWORD(UsernamePasswordAuthHandler.class),
    /**
     * 邮箱+密码
     */
    EMAIL_PASSWORD(EmailPasswordAuthHandler.class),
    /**
     * 手机号+验证码
     */
    PHONE_CODE(PhoneCodeAuthHandler.class);

    private final Class<?> clazz;

    LoginEnum(Class<?> clazz) {
        this.clazz = clazz;
    }

}