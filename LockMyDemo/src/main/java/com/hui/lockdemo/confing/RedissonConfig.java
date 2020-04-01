package com.hui.lockdemo.confing;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RedissonConfig {
    @Value("${redisson.host.config}")
    private String redisHost;
    @Bean
    public RedissonClient config() {
        Config config = new Config();
        // 设置传输模式
        config.setTransportMode(TransportMode.NIO);
        // 设置服务节点部署模式：集群模式，单一节点模式，主从模式，哨兵模式
        //使用单一节点模式
        config.useSingleServer().setAddress(redisHost).setKeepAlive(true);
        // 创建并返回redissonClient
        return Redisson.create(config);
    }
}

