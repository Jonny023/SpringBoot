package org.example.websocket.server;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.example.listener.SubscribeListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jonny
 * <p>@ServerEndpoint 注解是一个类层次的注解，
 * 它的功能主要是将目前的类定义成一个websocket服务器端,注解的值将被用于监听用户连接的终端访问URL地址,
 * 客户端可以通过这个URL来连接到WebSocket服务器端使用springboot的唯一区别是要@Component声明下，
 * 而使用独立容器是由容器自己管理websocket的，但在springboot中连容器都是spring管理的。</p>
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/server/{username}")
public class WebSocketServer {

    public static final String TOPIC_PREFIX = "websocket:topic:";

    /**
     * 因为@ServerEndpoint不支持注入，所以使用SpringUtil获取IOC实例
     */
    private final RedisMessageListenerContainer redisMessageListenerContainer = SpringUtil.getBean(RedisMessageListenerContainer.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的webSocket对象。
     * 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
     */
    private static final CopyOnWriteArraySet<WebSocketServer> WEB_SOCKET_SET = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * redis监听
     */
    private SubscribeListener subscribeListener;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        this.session = session;
        // 加入set中
        WEB_SOCKET_SET.add(this);
        // 在线数加1
        addOnlineCount();
        log.info("有新连接[{}]加入！当前在线人数为{}", username, getOnlineCount());
        subscribeListener = new SubscribeListener();
        subscribeListener.setSession(session);
        // 设置订阅topic
        redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic(TOPIC_PREFIX + username));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        // 从set中删除
        WEB_SOCKET_SET.remove(this);
        // 在线数减1
        subOnlineCount();
        redisMessageListenerContainer.removeMessageListener(subscribeListener);
        log.info("有一连接关闭！当前在线人数为{}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:{}", message);
        // 群发消息
        for (WebSocketServer item : WEB_SOCKET_SET) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                log.info("发送消息异常：msg = ", e);
                continue;
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session session
     * @param error   错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误", error);
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message 消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public int getOnlineCount() {
        return ONLINE_COUNT.get();
    }

    public void addOnlineCount() {
        WebSocketServer.ONLINE_COUNT.getAndIncrement();
    }

    public void subOnlineCount() {
        WebSocketServer.ONLINE_COUNT.getAndDecrement();
    }

}