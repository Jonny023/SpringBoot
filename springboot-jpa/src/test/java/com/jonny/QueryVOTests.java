package com.jonny;

import com.alibaba.fastjson.JSON;
import com.jonny.entity.vo.UserVO;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@SpringBootTest
public class QueryVOTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void exe() {

        //必须是hibernate的实entity类
        //entityManager.createNativeQuery(sql, EntityClass.class)

        //必须通过addScalar方法设置字段类型【普通的java类，如VO】
        Query nativeQuery = entityManager.createNativeQuery("select id, username from user");
        org.hibernate.query.Query query = nativeQuery.unwrap(NativeQuery.class)
                .addScalar("id", LongType.INSTANCE)
                .addScalar("username", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(UserVO.class));
        System.out.println(JSON.toJSONString(query.list()));
    }
}
