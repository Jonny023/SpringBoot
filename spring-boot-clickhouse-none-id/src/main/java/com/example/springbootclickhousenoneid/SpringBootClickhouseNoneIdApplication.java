package com.example.springbootclickhousenoneid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringBootClickhouseNoneIdApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootClickhouseNoneIdApplication.class, args);
  }

}
