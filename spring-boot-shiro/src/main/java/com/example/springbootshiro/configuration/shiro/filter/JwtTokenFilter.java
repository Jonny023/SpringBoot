package com.example.springbootshiro.configuration.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.example.springbootshiro.configuration.shiro.token.JwtToken;
import com.example.springbootshiro.configuration.shiro.utils.JwtUtil;
import com.example.springbootshiro.domain.enums.ResponseEnum;
import com.example.springbootshiro.domain.response.R;
import com.example.springbootshiro.domain.vo.UserDetailVO;
import lombok.extern.slf4j.Slf4j;
import ml.ytooo.redis.single.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Slf4j
public class JwtTokenFilter extends FormAuthenticationFilter {

    private int errorCode;
    private String errorMsg;

    //集群版
    //private static JedisClusterClient JEDIS = JedisClusterClient.getInstance();
    //单机版
    private static JedisClient JEDIS = JedisClient.getInstance();

    /**
     * 如果在这里返回了false，请求onAccessDenied()
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            this.errorCode = ResponseEnum.TOKEN_UNAVAILABLE.getCode();
            this.errorMsg = ResponseEnum.TOKEN_UNAVAILABLE.getMsg();
            result(response);
            return false;
        }
        log.info("sid: " + token);
        UserDetailVO userInfo = null;
        String username = null;
        try {
            username = JwtUtil.verifyToken(token).get("username").asString();
            String redisToken = JEDIS.getJedis().get(username);
            if (!Objects.equals(token, redisToken)) {
                this.errorCode = ResponseEnum.TOKEN_EXPIRE.getCode();
                this.errorMsg = ResponseEnum.TOKEN_EXPIRE.getMsg();
                result(response);
                return false;
            }
            //userInfo = JSON.parseObject(JEDIS.getJedis().get(username), UserDetailVO.class);
        } catch (Exception e) {
            this.errorCode = ResponseEnum.TOKEN_EXPIRE.getCode();
            this.errorMsg = ResponseEnum.TOKEN_EXPIRE.getMsg();
            result(response);
            return false;
        }
        //if (userInfo == null) {
        //    this.errorCode = ResponseEnum.TOKEN_EXPIRE.getCode();
        //    this.errorMsg = ResponseEnum.TOKEN_EXPIRE.getMsg();
        //    result(response);
        //    return false;
        //}
        //刷新超时时间
        JEDIS.expire(username, 30 * 60); //30分钟过期
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        result(response);
        return false;
    }

    private void result(ServletResponse response) {
        R result = R.error(this.errorCode, this.errorMsg);
        String responseJson = JSON.toJSONString(result);
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(responseJson);
        } catch (IOException e) {
            log.error("权限校验异常", e);
        }
    }
}
