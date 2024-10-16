package com.example.springbootlogin.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.example.springbootlogin.domain.context.UserContext;
import com.example.springbootlogin.domain.context.UserData;
import com.example.springbootlogin.domain.vo.R;
import com.example.springbootlogin.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 后台拦截器
 */
@Slf4j
public class BackgroundInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        response.setCharacterEncoding(UTF_8.name());
        response.setContentType("application/json;charset=utf-8");
        if (StrUtil.isEmpty(header)) {
            R unauthroized = R.errorMsg(401, "未授权");
            response.getWriter().write(JSONUtil.toJsonStr(unauthroized));
            return false;
        }
        try {
            boolean verify = JwtUtil.validateToken(header);
            if (!verify) {
                R result = R.errorMsg("无效token");
                response.getWriter().write(JSONUtil.toJsonStr(result));
                return false;
            }
            JWT jwt = JwtUtil.getJwt(header);
            UserData user = UserData.builder().id(JwtUtil.id(jwt)).username(JwtUtil.username(jwt)).build();
            UserContext.set(user);
        } catch (Exception e) {
            log.error("", e);
            R result = R.errorMsg("token验证失败");
            response.getWriter().write(JSONUtil.toJsonStr(result));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}