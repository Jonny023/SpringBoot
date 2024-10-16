package com.example.springbootlogin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.example.springbootlogin.constant.Const;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class JwtUtil {


    /**
     * 创建token
     *
     * @param payload token存储参数
     * @return token
     */
    public static String create(Map<String, Object> payload) {
        return JWTUtil.createToken(payload, Const.JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 校验token
     *
     * @param token token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        JWT jwt = getJwt(token);

        // 校验签名
        boolean isVerified = jwt.setKey(Const.JWT_SECRET_KEY.getBytes(UTF_8)).verify();
        if (!isVerified) {
            // 签名无效
            return false;
        }

        // 获取 exp（过期时间）
        Object expObj = jwt.getPayload("exp");
        if (expObj == null) {
            // token 中没有 exp 字段，视为无效
            return false;
        }

        long expTime = Long.parseLong(expObj.toString());
        long currentTime = DateUtil.current();

        // 判断是否过期
        if (expTime < currentTime) {
            // token 已过期
            return false;
        }
        return true;
    }

    /**
     * 用户ID
     *
     * @param jwt jwt
     * @return id
     */
    public static Long id(JWT jwt) {
        Object id = jwt.getPayload("uid");
        return Long.valueOf(id.toString());
    }

    /**
     * 用户名
     *
     * @param jwt jwt
     * @return id
     */
    public static String username(JWT jwt) {
        Object username = jwt.getPayload("username");
        return String.valueOf(username);
    }

    /**
     * 解析token
     *
     * @param token token
     * @return token对象
     */
    public static JWT getJwt(String token) {
        // 解析 token
        JWT jwt = JWTUtil.parseToken(token);
        return jwt;
    }
}
