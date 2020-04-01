package com.hui.redissondemo.message;

import com.hui.redissondemo.entity.UserLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserLoginSubscriber implements ApplicationRunner, Ordered {
    
    // 构造基于发布订阅的主题key
    private static final String topicKey = "redissonUserLoginTopicKey";
    
    @Autowired
    private RedissonClient redissonClient;
    
    /**
     * 在这个方法里实现“不断监听该主题中的消息的动态” -- 即间接的实现自动监听
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        RTopic rTopic = redissonClient.getTopic(topicKey);
        rTopic.addListener(UserLoginDto.class, new MessageListener<UserLoginDto>() {
            @Override
            public void onMessage(CharSequence charSequence, UserLoginDto userLoginDto) {
                
                log.info("记录用户登录成功后的轨迹--消费者--监听到的消息:{}",userLoginDto);
            }
        });
    }
    
    /**
     * 设置项目启动时，也跟着启动
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
