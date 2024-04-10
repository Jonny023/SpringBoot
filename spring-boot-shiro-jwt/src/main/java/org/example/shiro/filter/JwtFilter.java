package org.example.shiro.filter;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.example.domain.vo.R;
import org.example.shiro.token.JwtToken;
import org.example.shiro.util.ContextUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义一个Filter，用来拦截所有的请求判断是否携带Token
 * isAccessAllowed()判断是否携带了有效的JwtToken
 * onAccessDenied()是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问
 */
@Slf4j
public class JwtFilter extends AccessControlFilter {

    /**
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.warn("isAccessAllowed 方法被调用");
        //这里先让它始终返回false来使用onAccessDenied()方法
        return false;
    }

    /**
     * 返回结果为true表明登录通过
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.warn("onAccessDenied 方法被调用");

        // 这个地方和前端约定，要求前端将jwtToken放在请求的Header部分
        // 所以以后发起请求的时候就需要在Header中放一个Authorization，值就是对应的Token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        log.info("请求的 Header 中藏有 jwtToken {}", jwt);
        JwtToken jwtToken = new JwtToken(jwt);

        try {
            // 委托 realm 进行登录认证
            // 所以这个地方最终还是调用JwtRealm进行的认证
            getSubject(servletRequest, servletResponse).login(jwtToken);
            // 也就是subject.login(token)
        } catch (Exception e) {
            onLoginFail(servletResponse);
            // 调用下面的方法向客户端返回错误信息
            return false;
        }
        return true;
    }

    /**
     * 登录失败时默认返回 401 状态码
     *
     * @param response 响应
     */
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setCharacterEncoding("UTF-8");
        // resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        R<String> notCertified = R.error(401, "未登录");
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(JSONUtil.toJsonStr(notCertified));
            writer.flush();
        }
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        ContextUtil.remove();
    }
}