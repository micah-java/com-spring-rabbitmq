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

@Component
public class TopicConsumer02 {

    private final static Logger logger = LoggerFactory.getLogger(TopicConsumer02.class);

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = RabbitmqConstant.QUEUE_TOPIC,durable = "true",autoDelete = "false"),
            exchange = @Exchange(value = RabbitmqConstant.QUEUE_TOPIC_EXCHANGE,type = ExchangeTypes.TOPIC),
            key = {RabbitmqConstant.QUEUE_TOPIC_KEY}
        ),
        concurrency = "1"
    )
    public void topicConsumer02(Message message, @Header(name = "amqp_deliveryTag") long deliveryTag, Channel channel) throws IOException {
        logger.info("topic consumer02 message:{},deliveryTag:{}",new String(message.getBody(),"utf-8"),deliveryTag);
        channel.basicAck(deliveryTag,false);
    }
}
