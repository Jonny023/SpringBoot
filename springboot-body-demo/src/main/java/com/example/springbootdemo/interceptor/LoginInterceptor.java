package com.example.springbootdemo.interceptor;

import com.example.springbootdemo.servlet.BodyReaderWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("======================前置处理=======================");

        BodyReaderWrapper requestWrapper = new BodyReaderWrapper(request);
        if (requestWrapper.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {

            String body = StreamUtils.copyToString(requestWrapper.getInputStream(), Charset.defaultCharset());
            log.info("请求体1:{}", body);

            String body2 = StreamUtils.copyToString(requestWrapper.getInputStream(), Charset.defaultCharset());
            log.info("请求体2:{}", body2);
        }

        //校验签名、token等
        String token = request.getHeader("token");
        if (StringUtils.hasText(token)) {
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("======================后置处理=======================");
        //HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}