package com.hui.lockdemo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan(basePackages = "com.hui.lockdemo.mapper")
public class LockdemoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LockdemoApplication.class, args);
    }
    
    @Value("${zk.host}")
    private String zkHost;
    
    @Value("${zk.namespace}")
    private String zkNamespace;
    
    @Bean
    public CuratorFramework curatorFramework() {
    
        // 创建CuratorFramework 实例
        //1、使用工厂模式进行创建
        //2、指定了客户端连接到zookeeper服务端策略；这里重试的机制是5次，每次1s
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString(zkHost).namespace(zkNamespace).retryPolicy(
                        new RetryNTimes(5,1000)).build();
    
        curatorFramework.start();
        return curatorFramework;
        
    }
}
