package com.example.springbootjpanullvalue.hibernate;

import java.util.Map;
import org.hibernate.bytecode.enhance.spi.LazyPropertyInitializer;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.internal.DefaultMergeEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.property.access.internal.PropertyAccessStrategyBackRefImpl;
import org.hibernate.type.Type;

/**
 * @author: Jonny
 * @description: 对象拷贝时忽略null或空字符
 * @date:created in 2021/4/27 12:15
 * @modificed by:
 */
public class IgnoreNullEventListener extends DefaultMergeEventListener {

  public static final IgnoreNullEventListener INSTANCE = new IgnoreNullEventListener();

  @Override
  protected void copyValues(EntityPersister persistent, Object entity, Object target,
      SessionImplementor source, Map copyCache) {
    //源目标
    Object[] original = persistent.getPropertyValues(entity);
    //存储目标
    Object[] targets = persistent.getPropertyValues(target);

    Type[] types = persistent.getPropertyTypes();

    Object[] copied = new Object[original.length];
    int len = types.length;
    for (int i = 0; i < len; i++) {
      if (original[i] == null ||
          original[i] == LazyPropertyInitializer.UNFETCHED_PROPERTY ||
          original[i] == PropertyAccessStrategyBackRefImpl.UNKNOWN
      ) {
        copied[i] = targets[i];
      } else {
        copied[i] = types[i].replace(original[i], targets[i], source, target, copyCache);
      }
    }

    persistent.setPropertyValues(target, copied);
  }
}
