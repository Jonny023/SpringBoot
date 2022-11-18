package com.example.springstudy.service;

import com.example.springstudy.core.PortAnnotation;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @PortAnnotation
    private String port;

    public String test() {
        return port;
    }
}