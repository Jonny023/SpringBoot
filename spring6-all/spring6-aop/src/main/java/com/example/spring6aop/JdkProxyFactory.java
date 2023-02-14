package com.example.spring6aop;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * JDK动态代理
 */
@Slf4j
public class JdkProxyFactory implements InvocationHandler {

    private Object target;

    public JdkProxyFactory(Object target) {
        this.target = target;
    }

    /**
     * @param proxy  代理对象
     * @param method 目标对象的方法
     * @param args method方法对应的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("aop start: {}, 参数: {}", method.getName(), Arrays.toString(args));
        Object result = method.invoke(target, args);
        log.info("aop end: {}, 结果: {}", method.getName(), result);
        return result;
    }

    //public Object getProxy() {
    //    Class<?> clazz = target.getClass();
    //    ClassLoader classLoader = clazz.getClassLoader();
    //    Class<?>[] interfaces = clazz.getInterfaces();
    //    InvocationHandler invocationHandler = (proxy, method, args) -> {
    //        log.info("aop日志: {}, 参数: {}", method.getName(), Arrays.toString(args));
    //        Object result = method.invoke(target, args);
    //        log.info("aop日志: {}, 结果: {}", method.getName(), result);
    //        return result;
    //    };
    //    return Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
    //}

    public Object getProxy() {
        Class<?> clazz = target.getClass();
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?>[] interfaces = clazz.getInterfaces();
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }
}