package com.example.springbootshiro;

import com.auth0.jwt.interfaces.Claim;
import com.example.springbootshiro.configuration.shiro.utils.JwtUtil;

import java.util.Arrays;
import java.util.Map;

public class Demo {

    public static void main(String[] args) {
        String str = "a\\b\\c\\d";
        System.out.println(Arrays.toString(str.split("\\\\")));

        String token = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE2NTU3OTk5NTgsImlhdCI6MTY1NTc5OTg5OCwidXNlcm5hbWUiOiJhZG1pbiJ9.A0d2Lyds_BZuw4Ik_4L3Lh0-2tuvRWnuRaENQ5NxrY4";
        try {
            Map<String, Claim> stringClaimMap = JwtUtil.verifyToken(token);
            System.out.println(stringClaimMap);
            System.out.println(stringClaimMap.get("username").asString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
