package com.hui.rabbitmqdemo.conf;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {
    
    @Value("${mq.topic.queue.one}")
    private String topicQueueOne;
    
    @Value("${mq.topic.queue.two}")
    private String topicQueueTwo;
    
    @Value("${mq.topic.exchange.name}")
    private String topicExchangeName;
    
    @Value("${mq.topic.key.one.name}")
    private String topicKeyOneName;
    
    @Value("${mq.topic.key.two.name}")
    private String topicKeyTwoName;
    /**
     * 创建持久化队列1
     * @return 队列
     */
    @Bean(name="topicQueueOne")
    public Queue topicQueueOne() {
        return new Queue(topicQueueOne,true);
    }
    
    /**
     * 创建持久化队列2
     * @return 队列
     */
    @Bean(name="topicQueueTwo")
    public Queue topicQueueTwo() {
        return new Queue(topicQueueTwo,true);
    }
    
    /**
     * 创建交换机，交换机持久化，不自动删除
     * @return 交换机
     */
    @Bean(name="topicExchange")
    public TopicExchange topicExchange(){
        return new TopicExchange(topicExchangeName,true, false);
    }
    
    
    /**
     * 创建绑定1  把队列1和交换机通过 routekey 来进行绑定
     * @return
     */
    @Bean
    public Binding topicBindingOne(TopicExchange topicExchange, Queue topicQueueOne) {
        return BindingBuilder.bind(topicQueueOne).to(topicExchange).with(topicKeyOneName);
    }
    
    /**
     * 创建绑定2  把队列和交换机通过 routekey 来进行绑定
     * @return
     */
    @Bean
    public Binding topicBindingTwo(TopicExchange topicExchange, Queue topicQueueTwo) {
        return BindingBuilder.bind(topicQueueTwo).to(topicExchange).with(topicKeyTwoName);
    }
}
