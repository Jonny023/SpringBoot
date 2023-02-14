package com.example.spring6aop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    private int step = 1;

    @Pointcut("execution(* com.example.spring6aop.service.UserServiceImpl.*(..))")
    public void pointCut() {

    }

    //@Before("execution(* com.example.spring6aop.service.UserServiceImpl.*(..))")
    //@Before(value = "execution(public String com.example.spring6aop.service.UserServiceImpl.add(..))")
    @Before("com.example.spring6aop.aop.LogAspect.pointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        log.info("{} 前置通知", step++);
        log.info("调用方法:{}, 参数: {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    //@After("execution(* com.example.spring6aop.service.*.*(..))")
    @After("pointCut()")
    public void afterMethod() {
        log.info("{} 后置通知", step++);
    }

    //@AfterReturning(value = "execution(* com.example.spring6aop.service.*.*(..))", returning = "result")
    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        log.info("{} 返回通知", step++);
        log.info("返回数据: {}", result);
    }

    //@AfterThrowing(value = "execution(* com.example.spring6aop.service.*.*(..))", throwing = "ex")
    @AfterThrowing(value = "pointCut()", throwing = "ex")
    public void AfterThrowingMethod(JoinPoint joinPoint, Throwable ex) {
        log.info("{} 异常通知", step++);
        log.info("异常信息: ", ex);
    }

    //@Around(value = "execution(* com.example.spring6aop.service.*.*(..))")
    @Around(value = "pointCut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            log.info("{} 环绕通知 目标方法前", step++);
            result = joinPoint.proceed();
            log.info("{} 环绕通知 目标方法后", step++);
        } catch (Throwable throwable) {
            log.info("{} 环绕通知 目标方法异常", step++);
        } finally {
            log.info("{} 环绕通知 目标方法finally", step++);
        }
        return result;
    }
}