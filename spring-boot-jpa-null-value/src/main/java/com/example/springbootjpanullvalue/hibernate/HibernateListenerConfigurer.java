package com.example.springbootjpanullvalue.hibernate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Jonny
 * @description:
 * @date:created in 2021/4/27 12:19
 * @modificed by:
 */
@Configuration
public class HibernateListenerConfigurer {

  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;

  @PostConstruct
  protected void init() {
    SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
    EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    registry.getEventListenerGroup(EventType.MERGE).clear();
    registry.getEventListenerGroup(EventType.MERGE).prependListener(IgnoreNullEventListener.INSTANCE);
  }

}
