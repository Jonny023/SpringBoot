package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.entity.User;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import com.example.demo.service.RedisCacheStorageService;
import com.example.demo.config.RedisUtil;

@Service("redisCacheStorageServiceImpl")
//@Service("redisCacheStorageService")
public class RedisCacheStorageServiceImpl<V> implements RedisCacheStorageService<String,V>{

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 默认过时时间
     */
    private static final int EXPRIE_TIME =3600*24;

    /**
     * 获取Jedis相关操作
     */
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean set(String key, V value) {

        return set(key,value,EXPRIE_TIME);
    }

    @Override
    public boolean set(String key, V value, int exp) {
        Jedis jedis=null;
        if(StringUtils.isEmpty(key)){
            return  false;
        }
        try {
            //获取jedis对象
            jedis= redisUtil.getResource();
            //使用对象转换为Json格式插入redis
            JSONObject json = JSONObject.fromObject(value);//将java对象转换为json对象
            String jsonValue = json.toString();//将json对象转换为json字符串
            log.info("客户端:{}", jedis);
            jedis.setex(key,exp,jsonValue);
        }catch (Exception e){
            log.error("异常:{}", e);
            //释放jedis对象
            redisUtil.brokenResource(jedis);
            log.info("client can't connect server");
            return  false;
        }finally {
            redisUtil.returnResource(jedis);//返还连接池
            return true;
        }
    }

    @Override
    public V get(String key,Object object) {
        Jedis jedis=null;
        V v=null;
        if(StringUtils.isEmpty(key)){
            log.info("redis取值，key为空");
            return  null;
        }
        try{
            jedis=redisUtil.getResource();  //获取连接
            String jsonValue=jedis.get(key);   //从redis得到值,得到的是json字符串，因为我们之前插入的时候是使用的json字符串
            if(StringUtils.isEmpty(jsonValue)){
                return  null;
            }
            JSONObject obj = new JSONObject().fromObject(jsonValue);//将json字符串转换为json对象
            v = (V)JSONObject.toBean(obj,object.getClass());//将建json对象转换为你想要的java对象
            return v;
        }catch (Exception e){
            log.error("异常:{}", e);
            //释放jedis对象
            if(jedis!=null){
                redisUtil.brokenResource(jedis);
            }
            log.info("client can't get value");
            return  null;
        }finally {
            //返还连接池
            redisUtil.returnResource(jedis);

        }

    }

    @Override
    public boolean remove(String key) {
        Jedis jedis=null;
        try{
            jedis=redisUtil.getResource();
            if(StringUtils.isEmpty(key)){
                log.info("redis取值，key为空");
                return  false;
            }
            jedis.del(key);
        }catch (Exception e) {
            log.error("异常:{}", e);
            //释放jedis对象
            if(jedis!=null){
                redisUtil.brokenResource(jedis);
            }
            log.info("  del fail from redis");
            return false;

        }finally{
            //返还连接池
            redisUtil.returnResource(jedis);
            return true;
        }



    }

    @Override
    public boolean hset(String cacheKey, String key, V value) {
        Jedis jedis =null;
        //将key 和value  转换成 json 对象
        JSONObject json = JSONObject.fromObject(cacheKey);//将java对象转换为json对象
        String jCacheKey = json.toString();//将json对象转换为json字符串
        JSONObject json2 = JSONObject.fromObject(value);//将java对象转换为json对象
        String jsonValue = json2.toString();//将json对象转换为json字符串
        //操作是否成功
        boolean isSucess =true;
        if(StringUtils.isEmpty(jCacheKey)){
            log.info("cacheKey is empty");
            return false;
        }
        try {
            jedis =redisUtil.getResource();
            //执行插入哈希
            jedis.hset(jCacheKey, key, jsonValue);
        } catch (Exception e) {
            log.info("client can't connect server");
            isSucess =false;
            if(null !=jedis){
                //释放jedis 对象
                redisUtil.brokenResource(jedis);
            }
            return false;
        }finally{
            if (isSucess) {
                //返还连接池
                redisUtil.returnResource(jedis);
            }
            return true;
        }
    }

    @Override
    public V hget(String cacheKey, String key,Object object) {
        Jedis jedis =null;
        V v =null;

        JSONObject json = JSONObject.fromObject(cacheKey);//将java对象转换为json对象
        String jCacheKey = json.toString();//将json对象转换为json字符串


        if(StringUtils.isEmpty(jCacheKey)){
            log.info("cacheKey is empty");
            return null;
        }
        try {
            //获取客户端对象
            jedis =redisUtil.getResource();
            //执行查询
            String jsonValue =  jedis.hget(jCacheKey, key);
            //判断值是否非空
            if(StringUtils.isEmpty(jsonValue)){
                return null;
            }else{

                JSONObject obj = new JSONObject().fromObject(jsonValue);//将json字符串转换为json对象

                v = (V)JSONObject.toBean(obj,object.getClass());//将建json对象转换为java对象

            }
            //返还连接池
            redisUtil.returnResource(jedis);
        } catch (JedisException e) {
            log.info("client can't connect server");
            if(null !=jedis){
                //redisUtil 对象
                redisUtil.brokenResource(jedis);
            }
        }
        return v;
    }

    @Override
    public Map<String, V> hget(String cacheKey,Object object) {

        JSONObject json = JSONObject.fromObject(cacheKey);//将java对象转换为json对象
        String jCacheKey = json.toString();//将json对象转换为字符串
        //非空校验
        if(StringUtils.isEmpty(jCacheKey)){
            log.info("cacheKey is empty!");
            return null;
        }
        Jedis jedis =null;
        Map<String,V> result =null;
        V v=null;
        try {
            jedis =redisUtil.getResource();
            //获取列表集合 因为插入redis的时候是jsonString格式，所以取出来key是String value也是String
            Map<String,String> map = jedis.hgetAll(jCacheKey);

            if(null !=map){
                for(Map.Entry<String, String> entry : map.entrySet()){
                    if(result ==null){
                        result =new HashMap<String,V>();
                    }

                    JSONObject obj = new JSONObject().fromObject(entry.getValue());//将json字符串转换为json对象
                    v = (V)JSONObject.toBean(obj,object.getClass());//将建json对象转换为java对象

                    result.put(entry.getKey(), v);
                }
            }
        } catch (JedisException e) {
            log.info("client can't connect server");
            if(null !=jedis){
                //释放jedis 对象
                redisUtil.brokenResource(jedis);
            }
        }
        return result;
    }


    @Override
    public boolean exists(String sessionid) {
        User user=(User) get(sessionid, new User());
        if(user!=null){
            return true;
        }
        return false;
    }
}