package com.example.springbootdemo.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 缓存body数据
 */
@Slf4j
@Order(Integer.MIN_VALUE)
@WebFilter(filterName = "bodyReaderFilter", urlPatterns = "/*")
public class BodyReaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("==================执行过滤器================");

        //HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contentType = request.getContentType();

        //判断请求类型
        if (contentType == null) {
            //表单请求
            chain.doFilter(request, response);
        } else if (contentType.startsWith("multipart/")) {
            //文件上传类型
            chain.doFilter(request, response);
        } else {
            if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                //如果是application/x-www-form-urlencoded, 参数值在request body中以 a=1&b=2&c=3...形式存在，
                //若直接构造BodyReaderHttpServletRequestWrapper，在将流读取并存到copy字节数组里之后,
                //httpRequest.getParameterMap()将返回空值！
                //若运行一下 httpRequest.getParameterMap(), body中的流将为空! 所以两者是互斥的！
                request.getParameterMap();
            }
            ServletRequest requestWrapper = null;
            if (request instanceof HttpServletRequest) {
                requestWrapper = new BodyReaderWrapper((HttpServletRequest) request);
            }
            if (requestWrapper == null) {
                chain.doFilter(request, response);
            } else {
                chain.doFilter(requestWrapper, response);
            }
        }

    }

    @Override
    public void destroy() {

    }
}