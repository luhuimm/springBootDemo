package com.hui.lockdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hui.lockdemo.entity.UserReg;
import com.hui.lockdemo.mapper.UserRegMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class UserRegService {
    
    @Autowired
    private RedissonClient redissonClient;
    
    @Autowired
    private UserRegMapper userRegMapper;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 不加锁的处理
     * @param userName 用户名
     * @param password 密码
     * @throws Exception 异常
     */
    public void userRegNoLock(String userName, String password) throws Exception {
        // 根据用户名进行查询
        UserReg reg = userRegMapper.selectOne(new QueryWrapper<UserReg>().eq("user_name",userName));
        if (reg == null) {
            log.info("----不加分布式锁---,当前用户名为：{}", userName);
            // 把用户信息注册到数据库
            UserReg entity = new UserReg();
            entity.setUserName(userName);
            entity.setPassword(password);
            entity.setCreateTime(new Date());
            userRegMapper.insert(entity);
            log.info("用户名：{} 已经注册成功",userName);
        } else {
            throw  new Exception("--用户信息已经存在--");
        }
    }
    
    /**
     * 加锁的处理
     * @param userName 用户名
     * @param password 密码
     */
    public void userRegWithLock(String userName, String password) throws Exception {
    
        /*
        精心设计并构造setnx 操作中的 key
         */
        final String key = userName+"_lock";
    
        /**
         * 这里采用ns的时间措+uuid 生产随机数作为value
         */
        final String value = System.nanoTime()+ "" + UUID.randomUUID();
    
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
    
        //调用 setnx 操作 返回true表示成功
        Boolean res = valueOperations.setIfAbsent(key,value);
        
        if(res) {
            
            try {
                // 为了防止死锁的状况，加上 expire操作 为 20s
                stringRedisTemplate.expire(key,20L, TimeUnit.SECONDS);
                // 根据用户名进行查询
                UserReg reg = userRegMapper.selectOne(new
                        QueryWrapper<UserReg>().eq("user_name",userName));
                if (reg == null) {
                    log.info("----不加分布式锁---,当前用户名为：{}", userName);
                    // 把用户信息注册到数据库
                    UserReg entity = new UserReg();
                    entity.setUserName(userName);
                    entity.setPassword(password);
                    entity.setCreateTime(new Date());
                    userRegMapper.insert(entity);
                    log.info("用户名：{} 已经注册成功",userName);
                } else {
                    // 如果用户存在就抛出异常
                    throw  new Exception("--用户信息已经存在--");
                }
            }
            finally {
                // 不管发生了什么情况，都要释放锁
                if (value.equals(valueOperations.get(key).toString())) {
                    stringRedisTemplate.delete(key);
                }
            }
         
        }
    }
    
    /**
     * Redisson加锁的处理
     * @param userName 用户名
     * @param password 密码
     */
    public void userRegRedisson(String userName,String password) throws Exception {
        final String lockName = "redissonOneLock-" + userName;
        // 获取分布式锁
        RLock rLock = redissonClient.getLock(lockName);
        try {
            // 上锁 不管什么情况10s 后释放锁
            rLock.lock(10,TimeUnit.SECONDS);
            // 根据用户名进行查询
            UserReg reg = userRegMapper.selectOne(new
                    QueryWrapper<UserReg>().eq("user_name",userName));
            if (reg == null) {
                log.info("----不加分布式锁---,当前用户名为：{}", userName);
                // 把用户信息注册到数据库
                UserReg entity = new UserReg();
                entity.setUserName(userName);
                entity.setPassword(password);
                entity.setCreateTime(new Date());
                userRegMapper.insert(entity);
                log.info("用户名：{} 已经注册成功",userName);
            } else {
                // 如果用户存在就抛出异常
                throw  new Exception("--用户信息已经存在--");
            }
            
        } finally {
            // 不管什么情况，都要释放锁
            if (rLock != null) {
                rLock.unlock();
            }
        }
    }
}
