package com.spring.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 消息没有正确到达队列时触发回调，如果正确到达队列则不执行
 */
@Component("producerReturnCallback")
public class ProducerReturnCallback implements RabbitTemplate.ReturnCallback {
    private final static Logger logger = LoggerFactory.getLogger(ProducerReturnCallback.class);

    //判断消息从exchange到queue是否成功
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("应答码:{},消息主体:{},描述:{}",replyCode,message.getBody().toString(),replyText);
        logger.info("消息的exchange:{},routingKey:{}",exchange,routingKey);
    }
}
