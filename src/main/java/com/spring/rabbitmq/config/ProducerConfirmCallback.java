package com.spring.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component("producerConfirmCallback")
public class ProducerConfirmCallback implements RabbitTemplate.ConfirmCallback {

    private final static Logger logger = LoggerFactory.getLogger(ProducerConfirmCallback.class);
    //消息是否到达exchange
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
       if(ack){
           //logger.info("消息发送到exchange成功:{}",correlationData);
       }else {
           logger.info("消息发送到exchange失败:{}",s);
       }
    }
}
