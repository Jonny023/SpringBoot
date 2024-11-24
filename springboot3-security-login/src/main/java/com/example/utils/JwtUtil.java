package com.example.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jonny
 */
public class JwtUtil {

    private static final String SECRET_KEY = "your-secret-key";
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000; // 7天

    public static String createToken(Long userId, String username) {
        DateTime now = DateTime.now();
        DateTime expireTime = now.offsetNew(DateField.MILLISECOND, (int) EXPIRE_TIME);

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("iat", now.getTime());
        payload.put("exp", expireTime.getTime());

        return JWTUtil.createToken(payload, SECRET_KEY.getBytes());
    }

    public static boolean verify(String token) {
        try {
            JWT.of(token).setKey(SECRET_KEY.getBytes()).validate(0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static JSONObject getPayload(String token) {
        return JWT.of(token).getPayload().getClaimsJson();
    }
}