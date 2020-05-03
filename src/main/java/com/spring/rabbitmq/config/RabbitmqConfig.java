package com.spring.rabbitmq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitmqConfig {

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setChannelCacheSize(50);
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         RetryTemplate retryTemplate,
                                         ProducerConfirmCallback producerConfirmCallback,
                                         ProducerReturnCallback producerReturnCallback){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setConfirmCallback(producerConfirmCallback);
        rabbitTemplate.setReturnCallback(producerReturnCallback);
        rabbitTemplate.setMessageConverter(new RabbitJsonConverter());
        return rabbitTemplate;
    }

    @Bean
    public RetryTemplate retryTemplate(RetryPolicy retryPolicy){
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    @Bean
    public RetryPolicy retryPolicy(){
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        return retryPolicy;
    }

    //配置消费者监听的容器
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置确认模式手工确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(new RabbitJsonConverter());
        return factory;
    }

    //声明topic交换机
    @Bean(RabbitmqConstant.QUEUE_TOPIC_EXCHANGE)
    public Exchange topicExchange01(){
        //durable:true 持久化
        return ExchangeBuilder.topicExchange(RabbitmqConstant.QUEUE_TOPIC_EXCHANGE).durable(true).build();
    }

    //声明topic交换机
    @Bean(RabbitmqConstant.QUEUE_DEAD_LETTER_EXCHANGE)
    public Exchange deadLetterExchange(){
        //durable:true 持久化
        return ExchangeBuilder.topicExchange(RabbitmqConstant.QUEUE_DEAD_LETTER_EXCHANGE).durable(true).build();
    }
}
