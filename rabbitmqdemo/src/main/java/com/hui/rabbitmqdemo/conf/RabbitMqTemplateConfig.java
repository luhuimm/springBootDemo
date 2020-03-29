package com.hui.rabbitmqdemo.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@Slf4j
public class RabbitMqTemplateConfig {
    
    @Value("${mq.manual.Queue}")
    private String manualQueue;

    @Autowired
    private CachingConnectionFactory connectionFactory;
    
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer;
    
    @Bean
    public RabbitTemplate rabbitTemplate() {
        //设置发送消息，进行确认
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        //设置发送消息后，返回确认信息
        connectionFactory.setPublisherReturns(true);
    
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        Set the mandatory flag when sending messages; only applies if a
//                * {@link #setReturnCallback(RabbitTemplate.ReturnCallback) returnCallback} had been provided.
        rabbitTemplate.setMandatory(true);
    
        // 消息发送成功进行确认处理
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        
                log.info("消息发送成功:correlationData={}，ack={},cause={}",correlationData,ack,cause);
            }
        });
    
        // 消息发送失败，输出日志
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息丢失:message={}，replyCode={},replyText={},exchange={},routingKey={}",message,replyCode,replyText,
                        exchange,routingKey);
            }
        });
        
        return rabbitTemplate;
    }
    
    /**
     * 单一消费者-确认模式为 Auto
     * @return
     */
    @Bean(name="simpleRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        
        simpleRabbitListenerContainerFactoryConfigurer.configure(factory,connectionFactory);
       // 设置连接工厂
        factory.setConnectionFactory(connectionFactory);
//        // 设置消息在传播中的格式
    //        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置消费者并发实例，在这里采用单一的模式
        factory.setConcurrentConsumers(1);
        // 设置消费者最大数量的实例
        factory.setMaxConcurrentConsumers(1);
        // 设置消费者每个并发实例预拉取的消息数量
        factory.setPrefetchCount(1);
        // 设置消息确认模式为自动消费模式
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }
    
}
