package com.hui.rabbitmqdemo.conf;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FantoutConfig {
    
    @Value("${mq.fanout.queue.one}")
    private String fanoutQueueOne;
    
    @Value("${mq.fanout.queue.two}")
    private String fanoutQueueTwo;
    
    @Value("${mq.fanout.exchange.name}")
    private String fanoutExchangeName;
    /**
     * 创建持久化队列1
     * @return 队列
     */
    @Bean(name="fanoutQueueOne")
    public Queue fanoutQueueOne() {
        return new Queue(fanoutQueueOne,true);
    }
    
    /**
     * 创建持久化队列2
     * @return 队列
     */
    @Bean(name="fanoutQueueTwo")
    public Queue fanoutQueueTwo() {
        return new Queue(fanoutQueueTwo,true);
    }
    
    /**
     * 创建交换机，交换机持久化，不自动删除
     * @return 交换机
     */
    @Bean(name="fanoutExchange")
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(fanoutExchangeName,true, false);
    }
    
    
    /**
     * 创建绑定1  把队列1和交换机通过 routekey 来进行绑定
     * @return
     */
    @Bean
    public Binding fanoutBindingOne(FanoutExchange fanoutExchange, Queue fanoutQueueOne) {
        return BindingBuilder.bind(fanoutQueueOne).to(fanoutExchange);
    }
    
    /**
     * 创建绑定2  把队列和交换机通过 routekey 来进行绑定
     * @return
     */
    @Bean
    public Binding fanoutBindingTwo(FanoutExchange fanoutExchange, Queue fanoutQueueTwo) {
        return BindingBuilder.bind(fanoutQueueTwo).to(fanoutExchange);
    }
    
}
