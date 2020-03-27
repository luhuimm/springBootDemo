package com.hui.redisdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.hui.redisdemo.mapper.ItemMapper;
import com.hui.redisdemo.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

// 缓存穿透service
@Service
@Slf4j
public class CachePassService {
    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String keyPrefix = "item:";
    
    public Item getItemInfo(String itemCode) throws JsonProcessingException {
        Item item = null;
        final String key = keyPrefix + itemCode;
    
        ValueOperations valueOperations = redisTemplate.opsForValue();
        
        if(redisTemplate.hasKey(key)) {
            Object res = valueOperations.get(key);
            if(res != null && !Strings.isNullOrEmpty(res.toString())) {
                item = objectMapper.readValue(res.toString(),Item.class);
            }
        } else {
            log.info("--获取商品信息-缓存中不存在该商品--从数据库中获取编号：{}",itemCode);
            QueryChainWrapper<Item> query = new QueryChainWrapper<>(itemMapper);
            
             item = itemMapper.selectOne(new QueryWrapper<Item>().lambda()
                    .eq(Item::getCode,itemCode));
             
            if (item != null) {
                valueOperations.set(key,objectMapper.writeValueAsString(item));
            } else {
                valueOperations.set(key,"",2L, TimeUnit.MINUTES);
            }
        }
        return item;
    }
}
