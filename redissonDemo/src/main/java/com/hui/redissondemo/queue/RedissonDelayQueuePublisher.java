package com.hui.redissondemo.queue;

import com.hui.redissondemo.entity.DeadDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonDelayQueuePublisher {
    @Autowired
    private RedissonClient redissonClient;
    
    public void sendDelayMsg(final DeadDto msg, final Long ttl) {
        final String dealyQueueName = "redissonDelayQueueV3";
        // 定义获取阻塞式队列实例
        RBlockingDeque<DeadDto> rBlockingDeque = redissonClient.getBlockingDeque(dealyQueueName);
        // 定义获取延迟队列实例
        RDelayedQueue<DeadDto> rDelayedQueue = redissonClient.getDelayedQueue(rBlockingDeque);
        //往延迟队列发送消息--设置TTL,相当于延迟了“阻塞队列” 中消息接收
        rDelayedQueue.offer(msg,ttl, TimeUnit.MILLISECONDS);
        
        log.info("Redisson延迟队列消息模型--生产者--发送消息入延迟队列-消息：{}",msg);
    }
}
