package com.example.springbootshiro;

import org.springframework.boot.SpringApplication;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableLdapRepositories
public class SpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }

}
