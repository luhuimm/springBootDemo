package com.hui.redisbuss.service.impl;

import com.hui.redisbuss.model.RedPacketDto;
import com.hui.redisbuss.service.IRedPacketService;
import com.hui.redisbuss.service.IRedService;
import com.hui.redisbuss.tool.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 如果已经抢过了，直接显示红包金额
        Object obj = valueOperations.get(redId+userId+":rob");
        if(obj != null) {
            return new BigDecimal(obj.toString());
        }
        // 点击红包
        Boolean res = click(redId);
        if(res) {
            // 随机获取一个红包
            Object value = redisTemplate.opsForList().rightPop(redId);
            if(value != null) {
                // 红包的总个数 -1
                String redTotalKey = redId +":total";
                Integer currentTotal = valueOperations.get(redTotalKey) != null ?
                        (Integer)valueOperations.get(redTotalKey):0;
                valueOperations.set(redTotalKey, currentTotal-1);
                
                //分为单位
                BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
                
                //用户抢到的红包入库
                redService.recordRobRedPacket(userId,redId,new BigDecimal(value.toString()));
                
                // 用户抢到的红包放入缓存
                valueOperations.set(redId+userId+":rob",result,24L, TimeUnit.HOURS);
                
                log.info("当前用户已经抢到红包了:userId ={}, key={}, 金额={}",userId,redId,result);
                
                return result;
            }
        }
        return null;
    }
    
    /**
     * 点红包的业务处理逻辑
     * @param redId 红包id
     * @return -true 代表有红包 否则没有红包
     */
    private Boolean click(String redId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String redTotalKey = redId+":total";
        Object total = valueOperations.get(redTotalKey);
        if(total != null && Integer.valueOf(total.toString()) > 0) {
            return true;
        }
        return false;
    }
}
