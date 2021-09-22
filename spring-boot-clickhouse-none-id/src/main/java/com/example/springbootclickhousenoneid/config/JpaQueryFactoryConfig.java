package com.example.springbootclickhousenoneid.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JpaQueryFactoryConfig {

  @Primary
  @Bean
  public JPAQueryFactory jpaQuery(@Qualifier("ckEntityManager") EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }
}
