package com.hui.redisbuss.service.impl;

import com.hui.redisbuss.model.RedPacketDto;
import com.hui.redisbuss.service.IRedPacketService;
import com.hui.redisbuss.service.IRedService;
import com.hui.redisbuss.tool.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class RedPacketService implements IRedPacketService {
    
    private static final String keyPrefix="redis:red:packet:";
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private IRedService redService;
    
    
    @Override
    public String handOut(RedPacketDto dto) throws Exception {
        if(dto.getAmount() > 0  && dto.getTotal() >0 ) {
            String timestamp = String.valueOf(System.nanoTime());
            List<Integer> list = RedPacketUtil.divideRedPackage(dto.getAmount(),dto.getTotal());
            String redId = new StringBuffer(keyPrefix).append(dto.getUserId()).
                    append(":").append(timestamp).toString();
            // 将随机金额列表存入
            redisTemplate.opsForList().leftPushAll(redId,list);
            
            String redTotalKey = redId  +":total";
            //将红包总数存入缓存中
            redisTemplate.opsForValue().set(redTotalKey,dto.getTotal());
    
            // 一步方式记录信息到数据库
            redService.recordRedPacket(dto,redId,list);
            
            return redId;
        } else {
            throw  new Exception("系统异常-分发红包-参数不合法!");
        }
    }
    
    @Override
    public BigDecimal rob(Integer userId, String redId) throws Exception {
        return null;
    }
}
