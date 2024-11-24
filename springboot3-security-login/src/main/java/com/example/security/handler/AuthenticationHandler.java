package com.example.security.handler;

import com.example.domain.vo.common.R;
import com.example.security.request.AuthRequest;

/**
 * @author Jonny
 */
public abstract class AuthenticationHandler {

    /**
     * 执行登录
     *
     * @param request 请求参数
     * @return 登录信息
     */
    public R<Object> doLogin(AuthRequest request) {
        return this.authenticate(request);
    }

    /**
     * 登录抽象方法
     *
     * @param request 请求参数
     * @return 登录信息
     */
    protected abstract R<Object> authenticate(AuthRequest request);

}