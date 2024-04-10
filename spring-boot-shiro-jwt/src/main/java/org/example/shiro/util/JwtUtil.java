package org.example.shiro.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;

import java.util.Map;

public class JwtUtil {

    /**
     * 私钥
     */
    private static final String KEY = "wtw";

    /**
     * 有效期1天
     */
    public static final int EXPIRE_TIME = 60 * 60 * 1000 * 12;

    /**
     * 生成token
     *
     * @param payload 载荷
     * @return token
     */
    public static String createToken(Map<String, Object> payload) {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.HOUR, EXPIRE_TIME);

        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);

        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);

        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);

        return JWTUtil.createToken(payload, KEY.getBytes());
    }

    /**
     * 校验token
     *
     * @param token token
     * @return token对象
     */
    public static JWTPayload parseToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        if (!jwt.setKey(KEY.getBytes()).verify()) {
            throw new RuntimeException("无效token");
        }
        if (!jwt.validate(0)) {
            throw new RuntimeException("token 已过期请重新登录");
        }
        return jwt.getPayload();
    }
}