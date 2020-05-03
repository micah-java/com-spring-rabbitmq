package com.spring.rabbitmq.controller;

import com.alibaba.fastjson.JSON;
import com.spring.rabbitmq.config.RabbitmqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    @ResponseBody
    public Map sendDeadLetter(){
        Map map = null;
        for (int i = 1; i <= 3;i++){
            map = new HashMap();
            map.put("number",i);
            rabbitTemplate.convertAndSend(RabbitmqConstant.QUEUE_DEAD_LETTER_EXCHANGE,RabbitmqConstant.QUEUE_DEAD_LETTER_KEY, JSON.toJSONString(map));
        }
        return map;
    }
}
