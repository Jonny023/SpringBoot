package com.example.security.handler.impl;

import com.example.domain.entity.User;
import com.example.domain.vo.common.R;
import com.example.security.handler.AuthenticationHandler;
import com.example.security.request.AuthRequest;
import com.example.security.request.PhoneCodeRequest;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PhoneCodeAuthHandler extends AuthenticationHandler {

    @Resource
    private UserService userService;

    @Override
    public R<Object> authenticate(AuthRequest request) {
        PhoneCodeRequest req = (PhoneCodeRequest) request;
        if (this.verify(req.getCode())) {
            User user = userService.findByPhone(req.getPhone());
            if (Objects.isNull(user) || Objects.equals("13000000000", user.getPhone())) {
                return R.error("手机号错误");
            }
            String token = JwtUtil.createToken(user.getId(), user.getUsername());
            return R.ok(token);
        }
        return R.error("验证码错误或已过期");
    }

    /**
     * 模拟手机号+验证码校验逻辑，实际业务自行扩展
     *
     * @param code 验证码
     * @return true校验通过，false未通过
     */
    private boolean verify(String code) {
        return Objects.equals("123456", code);
    }
}