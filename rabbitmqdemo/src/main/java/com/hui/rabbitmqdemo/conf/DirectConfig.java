package com.hui.rabbitmqdemo.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {
    
    @Value("${mq.direct.queue.one}")
    private String directQueueOne;
    
    @Value("${mq.direct.queue.two}")
    private String directQueueTwo;
    
    @Value("${mq.direct.exchange.name}")
    private String directExchangeName;
    
    @Value("${mq.direct.key.one.name}")
    private String directKeyOneName;
    
    @Value("${mq.direct.key.two.name}")
    private String directKeyTwoName;
    /**
     * 创建持久化队列1
     * @return 队列
     */
    @Bean(name="directQueueOne")
    public Queue directQueueOne() {
        return new Queue(directQueueOne,true);
    }
    
    /**
     * 创建持久化队列2
     * @return 队列
     */
    @Bean(name="directQueueTwo")
    public Queue directQueueTwo() {
        return new Queue(directQueueTwo,true);
    }
    
    /**
     * 创建交换机，交换机持久化，不自动删除
     * @return 交换机
     */
    @Bean(name="directExchange")
    public DirectExchange directExchange(){
        return new DirectExchange(directExchangeName,true, false);
    }
    
    
    /**
     * 创建绑定1  把队列1和交换机通过 routekey 来进行绑定
     * @return
     */
    @Bean
    public Binding directBindingOne(DirectExchange directExchange, Queue directQueueOne) {
        return BindingBuilder.bind(directQueueOne).to(directExchange).with(directKeyOneName);
    }
    
    /**
     * 创建绑定2  把队列和交换机通过 routekey 来进行绑定
     * @return
     */
    @Bean
    public Binding directBindingTwo(DirectExchange directExchange, Queue directQueueTwo) {
        return BindingBuilder.bind(directQueueTwo).to(directExchange).with(directKeyTwoName);
    }
}
