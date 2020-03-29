package com.hui.rabbitmqdemo.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnowlegAutoConf {

    @Value("${mq.auto.autoQueue}")
    private String autoQueue;
    
    @Value("${mq.auto.exchange.name}")
    private String autoExchangeName;
    
    @Value("${mq.auto.key.name}")
    private String autoKeyName;
    
    @Bean(name="autoQueue")
    public Queue autoQueue(){
        return new Queue(autoQueue,true);
    }
    
    @Bean
    public DirectExchange autoExchange() {
       return new DirectExchange(autoExchangeName,true,false);
    }
    
    @Bean
    public Binding autoBinding(Queue autoQueue, DirectExchange autoExchange) {
        return BindingBuilder.bind(autoQueue).to(autoExchange).with(autoKeyName);
    }
    
}
