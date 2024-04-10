package org.example.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 这个就类似UsernamePasswordToken
 */
public class JwtToken implements AuthenticationToken {

    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    /**
     * 类似用户名
     *
     * @return Object
     */
    @Override
    public Object getPrincipal() {
        return jwt;
    }

    /**
     * 类似密码
     *
     * @return Object
     */
    @Override
    public Object getCredentials() {
        return jwt;
    }
}