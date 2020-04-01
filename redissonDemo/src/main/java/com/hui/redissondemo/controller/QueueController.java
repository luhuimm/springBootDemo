package com.hui.redissondemo.controller;

import com.hui.redissondemo.entity.DeadDto;
import com.hui.redissondemo.queue.QueuePublisher;
import com.hui.redissondemo.queue.RedissonDelayQueuePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class QueueController {
    private static final String prefix = "/queue";
    @Autowired
    private QueuePublisher queuePublisher;
    
    @Autowired
    private RedissonDelayQueuePublisher redissonDelayQueuePublisher;
    
    @GetMapping(prefix+"/basic/msg/send")
    public String sendMsg(@RequestParam String msg) {
        queuePublisher.sendBasicMsg(msg);
        return msg;
    }
    @GetMapping(prefix+"/dead/msg/send")
    public String sendRedissonDelayMsg(){
        DeadDto msgA = new DeadDto(1, "A");
        final Long ttlA = 8000L;
        DeadDto msB = new DeadDto(2, "B");
        final Long ttlB = 2000L;
        DeadDto msgC = new DeadDto(3, "C");
        final Long ttlC = 4000L;
        redissonDelayQueuePublisher.sendDelayMsg(msgA,ttlA);
        redissonDelayQueuePublisher.sendDelayMsg(msB,ttlB);
        redissonDelayQueuePublisher.sendDelayMsg(msgC,ttlC);
        return "ok";
    }
}
