package com.example.springbootrabbitmqconsumer.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 读取配置中的队列
 */
@Component
public class Consumer1 {

    private Logger log = LoggerFactory.getLogger(Consumer1.class);

    /**
     * 组合使用监听
     *
     * @param message
     * @param channel
     * @throws Exception
     * @RabbitListener @QueueBinding @Queue @Exchange
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}", durable = "true"),
            exchange = @Exchange(name = "${spring.rabbitmq.listener.order.exchange.name}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "true"),
            key = "${spring.rabbitmq.listener.order.exchange.key}"
    )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        //	1. 收到消息以后进行业务端消费处理
        log.info("-----------读取配置队列------------");
        if (message.getPayload() instanceof byte[]) {
            log.info("消费消息: {}", new String((byte[]) message.getPayload(), "UTF-8"));
        } else {

            log.info("消费消息: {}", message.getPayload());
        }

        //  2. 处理成功之后 获取deliveryTag 并进行手工的ACK操作, 因为我们配置文件里配置的是 手工签收
        //	spring.rabbitmq.listener.simple.acknowledge-mode=manual
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }
}