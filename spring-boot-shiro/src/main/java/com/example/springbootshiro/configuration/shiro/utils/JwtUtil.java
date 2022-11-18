package com.example.springbootshiro.configuration.shiro.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springbootshiro.configuration.shiro.constant.AuthConst;
import com.example.springbootshiro.domain.dto.UserDTO;
import org.apache.shiro.authz.UnauthorizedException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    /**
     * 生成token
     *
     * @return
     * @throws Exception
     */
    public static String createToken(UserDTO userDTO) throws Exception {

        //签发时间
        Date iatDate = new Date();

        //过期时间-outTime分钟过期
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, AuthConst.EXPIRE_MINUTE);
        Date expiresDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(AuthConst.ALG_KEY, AuthConst.ALG_RS256);
        map.put(AuthConst.TYP_KEY, AuthConst.TYP_JWT);
        String token = JWT.create()
                .withHeader(map)
                .withClaim(AuthConst.USER_KEY, userDTO.getUsername())
                .withClaim(AuthConst.ID_KEY, userDTO.getId())
                .withClaim(AuthConst.ROLE_KEY, userDTO.getRoles())
                .withClaim(AuthConst.RIGHTS_KEY, userDTO.getRights())
                .withExpiresAt(expiresDate)//设置过期时间-过期时间要大于签发时间
                .withIssuedAt(iatDate)//设置签发时间
                .sign(Algorithm.HMAC256(AuthConst.SECRET));//加密
        return token;
    }

    /**
     * 解密token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) throws Exception {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(AuthConst.SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new UnauthorizedException("登录已失效,请重新登录");
        }
        return jwt.getClaims();
    }


}