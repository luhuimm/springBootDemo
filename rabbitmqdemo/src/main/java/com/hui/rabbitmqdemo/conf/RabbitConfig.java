package com.hui.rabbitmqdemo.conf;



import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class RabbitConfig {

    // 读取配置文件的环境变量实例
    @Autowired
    private Environment env;
    
    @Value("${mq.queue.name}")
    private String queueName;
    
    @Value("${mq.exchange.name}")
    private String exchangeName;
    
    @Value("${mq.route.key.name}")
    private String routeKeyName;
    
    /**
     * 创建持久化队列
     * @return 队列
     */
    @Bean(name="basicQueue")
    public Queue basicQueue() {
        return new Queue(queueName,true);
    }
    
    /**
     * 创建交换机，交换机持久化，不自动删除
     * @return 交换机
     */
    @Bean(name="basicExchange")
    public DirectExchange basicExchange(){
        return new DirectExchange(exchangeName,true, false);
    }
    
    /**
     * 创建绑定  把队列和交换机通过 routekey 来进行绑定
     * @return
     */
    @Bean
    public Binding binding(DirectExchange basicExchange, Queue basicQueue) {
        return BindingBuilder.bind(basicQueue).to(basicExchange()).with(routeKeyName);
    }
    
}
