package com.example.springstudy.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(value = {"com.example.springstudy"})
@PropertySource("classpath:application.yml")
public class BootConfig {
}