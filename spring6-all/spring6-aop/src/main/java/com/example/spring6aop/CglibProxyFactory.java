package com.example.spring6aop;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * cglib动态代理
 * JDK 8 中有关反射相关的功能自从 JDK 9 开始就已经被限制了，为了兼容原先的版本，需要在运行项目时添加vm options
 * --add-opens java.base/java.lang=ALL-UNNAMED
 * 选项来开启这种默认不被允许的行为。
 */
@Slf4j
public class CglibProxyFactory implements MethodInterceptor {

    private Object target;

    public CglibProxyFactory(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        log.info("cglib start: {}, 参数: {}", method.getName(), Arrays.toString(objects));
        Object result = methodProxy.invokeSuper(o, objects);
        log.info("cglib end: {}, 结果: {}", method.getName(), result);
        return result;
    }

    public Object getProxy() {
        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(this.target.getClass());
        // 设置enhancer的回调对象
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }
}
