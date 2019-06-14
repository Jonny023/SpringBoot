package com.example.demo.config;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisUtil {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private JedisPool jedisPool;


    /**
     * 获取jedis
     * @return
     */
    public Jedis getResource(){
        Jedis jedis =null;
        try {
            jedis =jedisPool.getResource();
        } catch (Exception e) {
            log.error("异常：{}", e);
            log.info("can't  get  the redis resource");
        }
        return jedis;
    }

    /**
     * 关闭连接
     * @param jedis
     */
    public void disconnect(Jedis jedis){
        jedis.disconnect();
    }

    /**
     * 将jedis 返还连接池
     * @param jedis
     */
    public void returnResource(Jedis jedis){
        if(null != jedis){
            try {
                jedisPool.returnResource(jedis);
            } catch (Exception e) {
                log.error("异常：{}", e);
                log.info("can't return jedis to jedisPool");
            }
        }
    }

    /**
     * 无法返还jedispool，释放jedis客户端对象，
     * @param jedis
     */
    public void brokenResource(Jedis jedis){
        if (jedis!=null) {
            try {
                jedisPool.returnBrokenResource(jedis);
            } catch (Exception e) {
                log.error("异常：{}", e);
                log.info("can't release jedis Object");
            }
        }
    }
}