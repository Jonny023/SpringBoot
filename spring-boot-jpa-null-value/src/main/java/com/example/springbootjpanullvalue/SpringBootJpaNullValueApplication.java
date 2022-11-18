package com.example.springbootjpanullvalue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class SpringBootJpaNullValueApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootJpaNullValueApplication.class, args);
  }

}
