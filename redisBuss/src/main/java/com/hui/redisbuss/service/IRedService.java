package com.hui.redisbuss.service;

import com.hui.redisbuss.model.RedPacketDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 红包业务逻辑处理过程中数据记录接口-一步实现
 */
public interface IRedService {

    // 记录发红包时红包的全局唯一标识，随机金额列表和个数等信息入库
    void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception;
    
    // 记录抢红包时，用户抢到的红包金额等信息入库
    void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception;
    
}
