package com.example.rabbitmq;

import com.example.entity.User;
import com.rabbitmq.client.AMQP;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqApplicationTests {

    // 注入管理组件
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {

        // direct模式只有对应的routingKey能接收消息
//        rabbitTemplate.convertAndSend("hello.direct", "user", "direct模式发送消息");

        // fanout模式，广播分发，不走路由键，只要是hello.fanout交换器绑定的队列都能收到消息
//        rabbitTemplate.convertAndSend("hello.fanout", "", "fanout模式发送消息");

        // topic模式，能够匹配的路有键都能收到消息
//        rabbitTemplate.convertAndSend("hello.topic", "*.news", "topic模式发送消息");
//        rabbitTemplate.convertAndSend("hello.topic", "user.#", "topic模式发送消息");

        // 序列化, 默认是byte字节流，可以通过自定义转换器Jackson2JsonMessageConverter转换为JSON字符串
        User user = new User(1L, "admin", "超级管理员", "123456");
//        rabbitTemplate.convertAndSend("hello.direct", "user", user);
        rabbitTemplate.convertAndSend("hello.direct", "user.news", user);

    }

    /**
     *  获取消息
     */
    @Test
    public void receive() {
        Object object = rabbitTemplate.receive("user.msg");
        System.out.println(object.getClass());
        System.out.println(object);
    }

    /**
     *  在程序中创建队列
     *  通过管理组件AmqpAdmin组件
     */
    @Test
    public void createQueue() {
        // 创建direct模式的交换器
//        rabbitAdmin.declareExchange(new DirectExchange("helloworld"));

        // 删除交换器
        rabbitAdmin.deleteExchange("helloworld");

        // 创建fanout模式的交换器
        rabbitAdmin.declareExchange(new FanoutExchange("fanout.exchange"));
        // 创建队列
        rabbitAdmin.declareQueue(new Queue("user.queue"));
        rabbitAdmin.declareBinding(new Binding("user.queue", Binding.DestinationType.QUEUE, "fanout.exchange", "user.add", null));
    }

}
