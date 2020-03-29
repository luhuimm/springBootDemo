package com.hui.rabbitmqdemo.conf;

import com.hui.rabbitmqdemo.rabbitmq.knowledge.ManualKnowledgeConsumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnowlegManalConfig {
    
    @Autowired
    private CachingConnectionFactory connectionFactory;
    
    @Autowired
    private ManualKnowledgeConsumer manualKnowledgeConsumer;
    
    @Value("${mq.manual.Queue}")
    private String manualQueue;
    
    @Value("${mq.manual.exchange.name}")
    private String manualExchangeName;
    
    @Value("${mq.manual.key.name}")
    private String manualKeyName;
    
    @Bean(name="manualQueue")
    public Queue manualQueue(){
        return new Queue(manualQueue,true);
    }
    
    @Bean
    public TopicExchange topicManualExchange() {
        return new TopicExchange(manualExchangeName,true,false);
    }
    
    @Bean
    public Binding manualBinding(Queue manualQueue, TopicExchange topicManualExchange) {
        return BindingBuilder.bind(manualQueue).to(topicManualExchange).with(manualKeyName);
    }
    
    @Bean
    public SimpleMessageListenerContainer simpleContainer(@Qualifier("manualQueue") Queue manualQueue){
    
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setPrefetchCount(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        
        container.setQueues(manualQueue);
        container.setMessageListener(manualKnowledgeConsumer);
        return container;
    
    }
    
}
