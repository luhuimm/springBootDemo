package com.hui.redissondemo.queue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Slf4j
@Component
public class QueueConsumer implements ApplicationRunner, Ordered {
    @Autowired
    private RedissonClient redissonClient;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 定义队列名称
        final String queueName = "redissonBasicQueue";
        // 获取队列实例
        RQueue<String> rQueue = redissonClient.getQueue(queueName);
        while (true) {
            String msg = rQueue.poll();
            if(!StringUtils.isEmpty(msg)) {
                log.info("队列的消息者--监听消费者消息{}",msg);
            }
        }
    }
    /**
     * 表示QueueConsumer将会在项目启动之后启动
     * @return
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
