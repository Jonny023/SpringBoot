package com.example.springbootshiro.configuration.shiro.constant;

import java.nio.charset.StandardCharsets;

public final class AuthConst {

    //jwt key
    public static byte[] SECRET = "3LNlwmS42sSLVafeCv_3WN+70c+5qkLE=z08u4nJKt9T8L60CJyP6wkd_zxSCueiqThXQj5ARidOZ84cep0ZhYVOzsmxM3pS6WZf6TstcRGCw-PmHZ9d7KRRfcJI+MqM".getBytes(StandardCharsets.UTF_8);

    //过期时间：30分钟
    public static int EXPIRE_MINUTE = 30;

    public static final String ALG_KEY = "alg";
    public static final String TYP_KEY = "type";
    public static final String TYP_JWT = "jwt";
    public static final String ALG_RS256 = "RS256";

    public static final String USER_KEY = "username";
    public static final String ID_KEY = "uid";
    public static final String ROLE_KEY = "roles";
    public static final String RIGHTS_KEY = "rights";
}