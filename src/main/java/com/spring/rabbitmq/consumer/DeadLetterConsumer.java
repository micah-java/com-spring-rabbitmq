package com.spring.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.spring.rabbitmq.config.RabbitmqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 死信队列
 */
@Component
public class DeadLetterConsumer {

    private final static Logger logger = LoggerFactory.getLogger(DeadLetterConsumer.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitmqConstant.QUEUE_DEAD_LETTER,durable = "true",autoDelete = "false",
                    arguments = {@Argument(name = "x-dead-letter-exchange",value = RabbitmqConstant.QUEUE_REDIRECT_EXCHANGE),
                            @Argument(name = "x-dead-letter-routing-key",value = RabbitmqConstant.QUEUE_REDIRECT_KEY)
                    }),
                    exchange = @Exchange(value = RabbitmqConstant.QUEUE_DEAD_LETTER_EXCHANGE,type = ExchangeTypes.TOPIC),
                    key = {RabbitmqConstant.QUEUE_DEAD_LETTER_KEY}
            ),
            concurrency = "1",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void topicConsumer02(Message message, @Header(name = "amqp_deliveryTag") long deliveryTag,@Header("amqp_redelivered") boolean redelivered ,Channel channel) throws IOException {
        try{
            logger.info("dead letter consumer message:{},deliveryTag:{}",new String(message.getBody(),"utf-8"),deliveryTag);
            int i = 1/0;
            //false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            if(redelivered){
                logger.info("消息重复处理失败，拒绝再次接收");
                //basicReject一次只能拒绝一条消息，false表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(deliveryTag,false);
            }else{
                logger.info("消息即将再次返回队列");
                //第一个boolean表示一个consumerhi还是所有
                //第二个boolean表示消息是否重回队列
                channel.basicNack(deliveryTag,false,true);
            }
        }
    }
}
