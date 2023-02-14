package com.example.spring6aop.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(value = {"com.example.spring6aop.aop", "com.example.spring6aop.service"})
public class MainConfig {
}
