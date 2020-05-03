package com.spring.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.spring.rabbitmq.config.RabbitmqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 死信队列，用来处理死信消息
 */
@Component
public class RedirectConsumer {

    private final static Logger logger = LoggerFactory.getLogger(RedirectConsumer.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitmqConstant.QUEUE_REDIRECT,durable = "false",autoDelete = "false"),
                    exchange = @Exchange(value = RabbitmqConstant.QUEUE_REDIRECT_EXCHANGE),
                    key = {RabbitmqConstant.QUEUE_REDIRECT_KEY}
            ),
            concurrency = "1"
    )
    public void redirectConsumer(Message message, @Header(name = "amqp_deliveryTag") long deliveryTag, Channel channel) throws IOException {
        logger.info("redirectConsumer message:{},deliveryTag:{}",new String(message.getBody(),"utf-8"),deliveryTag);
        channel.basicAck(deliveryTag,false);
    }
}
