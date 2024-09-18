package org.example.listener;

import cn.hutool.core.util.ObjectUtil;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

/**
 * @author Jonny
 */
@Slf4j
@Getter
@Setter
public class SubscribeListener implements MessageListener {

    /**
     * 当前websocket的session
     */
    private Session session;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String msg = new String(message.getBody());
        if (ObjectUtil.isNotEmpty(session) && session.isOpen()) {
            try {
                log.info("收到消息：{}", msg);
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                log.error("发送消息异常，msg = {} , e = ", msg, e);
            }
        }
    }
}