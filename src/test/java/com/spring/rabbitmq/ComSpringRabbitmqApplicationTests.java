package com.spring.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.spring.rabbitmq.config.RabbitmqConstant;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class ComSpringRabbitmqApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void topicProducer() {
        Map map = null;
        for (int i = 1; i <= 3;i++){
            map = new HashMap();
            map.put("age",i + 15);
            rabbitTemplate.convertAndSend(RabbitmqConstant.QUEUE_TOPIC_EXCHANGE,RabbitmqConstant.QUEUE_TOPIC_KEY, JSON.toJSONString(map));
        }
    }
}
