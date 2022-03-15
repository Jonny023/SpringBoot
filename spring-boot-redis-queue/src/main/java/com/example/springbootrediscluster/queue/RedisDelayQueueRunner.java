package com.example.springbootrediscluster.queue;

import com.example.springbootrediscluster.consumer.RedisDelayQueueHandler;
import com.example.springbootrediscluster.enums.RedisDelayQueueEnum;
import com.example.springbootrediscluster.utils.RedisDelayQueueUtil;
import com.example.springbootrediscluster.utils.SpringUtils;
import jodd.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDelayQueueRunner implements CommandLineRunner {
    
    private final Logger LOG = LoggerFactory.getLogger(RedisDelayQueueRunner.class);

    @Resource
    private RedisDelayQueueUtil redisDelayQueueUtil;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    ThreadPoolExecutor executorService = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000),new ThreadFactoryBuilder().setNameFormat("order-delay-%d").get());

    @Override
    public void run(String... args) throws Exception {

        threadPoolTaskExecutor.execute(() -> {
            while (true){
                try {
                    RedisDelayQueueEnum[] queueEnums = RedisDelayQueueEnum.values();
                    for (RedisDelayQueueEnum queueEnum : queueEnums) {
                        Object value = redisDelayQueueUtil.getDelayQueue(queueEnum.getCode());
                        if (value != null) {
                            RedisDelayQueueHandler<Object> redisDelayQueueHandler = SpringUtils.getBean("xxx");
                            executorService.execute(() -> {
                                redisDelayQueueHandler.execute(value);});
                        }
                    }
                } catch (InterruptedException e) {
                    LOG.error("(Redisson延迟队列监测异常中断) ", e);
                }
            }
        });
        LOG.info("(Redisson延迟队列监测启动成功)");
    }
}
