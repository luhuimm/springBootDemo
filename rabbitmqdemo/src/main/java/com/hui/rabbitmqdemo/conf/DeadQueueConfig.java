package com.hui.rabbitmqdemo.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DeadQueueConfig {
    
    @Value("${mq.dead.deadQueue}")
    private String deadQueue;
    
    @Value("${mq.dead.route.key}")
    private String deadRouteKey;
    
    @Value("${mq.dead.exchange.name}")
    private String deadExchangeName;
    
    
    @Value("${mq.real.queue.name}")
    private String realQueueName;
    
    
    @Value("${mq.basic.route.key}")
    private String basicRouteKey;
    
    @Value("${mq.basic.exchange.name}")
    private String basicExchangeName;
    
    /**
     * 死信队列消息构建
     * @return
     */
    @Bean
    public Queue basicDeadQueue() {
        Map<String,Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",deadExchangeName);
        args.put("x-dead-letter-routing-key",deadRouteKey);
        //单位为ms  就是 10s
        args.put("x-message-ttl",10000);
        return new Queue(deadQueue,true,false,false, args);
    }
    
    /**
     * 创建基本 “消息模型” 的基本交换机--面向生产者
     * @return
     */
    @Bean
    public TopicExchange basicProduceExchange(){
        return new TopicExchange(basicExchangeName,true, false);
    }
    
    /**
     * 创建基本 “消息模型” 的基本绑定-基本交换机—+基本路由  --面向生产者
     * @param basicDeadQueue
     * @param basicProduceExchange
     * @return
     */
    @Bean
    public Binding basicProduceBinding(Queue basicDeadQueue,TopicExchange basicProduceExchange) {
        return BindingBuilder.bind(basicDeadQueue).to(basicProduceExchange).with(basicRouteKey);
    }
    
    /**
     * 创建真正的队列---面向消费者
     * @return
     */
    @Bean
    public Queue realConsumerQueue(){
        return new Queue(realQueueName,true);
    }
    
    /**
     * 创建死信交换机
     * @return
     */
    @Bean
    public TopicExchange basicDeadExchange(){
        return new TopicExchange(deadExchangeName,true, false);
    }
    
    /**
     * 创建死信路由及其绑定
     * @return
     */
    @Bean
    public Binding basicDeadBinding(Queue realConsumerQueue,TopicExchange basicDeadExchange) {
        return BindingBuilder.bind(realConsumerQueue).to(basicDeadExchange).with(deadRouteKey);
    }
}
