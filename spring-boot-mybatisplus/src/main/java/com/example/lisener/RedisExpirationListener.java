package com.example.lisener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisExpirationListener extends KeyExpirationEventMessageListener {

    private Logger LOG = LoggerFactory.getLogger(RedisExpirationListener.class);

    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis key ttl数据处理（回调）
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        if (LOG.isInfoEnabled()) {
            LOG.info("redis过期key值监听, 线程名：{}", Thread.currentThread().getName());
        }

        String key = message.toString();
        if (key.split("_")[0].equals("PlayAddress")) {

        }

    }

}