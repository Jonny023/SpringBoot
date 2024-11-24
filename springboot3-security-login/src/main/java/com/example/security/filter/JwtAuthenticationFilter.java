package com.example.security.filter;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.domain.vo.common.R;
import com.example.security.SecurityConfig;
import com.example.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jonny
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isExcluded(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);

        if (StrUtil.isBlank(token)) {
            handleError(response, HttpStatus.UNAUTHORIZED, "未登录");
            return;
        }

        if (!JwtUtil.verify(token)) {
            handleError(response, HttpStatus.UNAUTHORIZED, "token无效或已过期");
            return;
        }

        JSONObject payload = JwtUtil.getPayload(token);
        String userId = payload.getStr("userId");
        String username = payload.getStr("username");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isExcluded(String requestUri) {
        return Arrays.stream(SecurityConfig.EXCLUDE_URLS).anyMatch(item -> new AntPathMatcher().match(item, requestUri));
    }

    /**
     * 获取请求头中的token
     *
     * @param request 请求
     * @return token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void handleError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        R<Object> unauthorized = R.error(status.value(), message);
        response.getWriter().write(JSONUtil.toJsonStr(unauthorized));
    }
}