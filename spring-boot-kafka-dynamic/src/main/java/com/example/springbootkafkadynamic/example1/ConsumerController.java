package com.example.springbootkafkadynamic.example1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    private final Logger LOG = LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    private ApplicationContext context;

    /**
     * 监控容器工厂
     */
    @Autowired
    private ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory;

    /**
     * @KafkaListener 这个注解中标注的所有方法都会在里面注册
     */
    @Autowired
    private KafkaListenerEndpointRegistry registry;

    /**
     * 创建消费组
     */
    @GetMapping("/create")
    public void create() {
        // 动态创建三个消费组
        String[] groupIds = {"group-0", "group-1", "group-2"};
        for (String groupId : groupIds) {
            // 初始化当前消费者组的配置
            Map<String, Object> consumerProps = new HashMap<>();
            consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.56.101:9092");
            consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringDeserializer");
            // 设置监听器容器工厂
            containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerProps));
            // 获取监听类实例
            context.getBean(MyListener.class);
        }
    }

    /**
     * 停止消费
     */
    @GetMapping("/stop")
    public void stop() {
        registry.getListenerContainers().forEach(container -> {
            LOG.info(container.getGroupId());
            LOG.info(container.getListenerId());
            container.stop();
        });
    }

    /**
     * 监听实例
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyListener listener() {
        return new MyListener();
    }
}
