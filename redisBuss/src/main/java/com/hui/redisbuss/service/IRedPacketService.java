package com.hui.redisbuss.service;

import com.hui.redisbuss.model.RedPacketDto;

import java.math.BigDecimal;

// 红包业务逻辑接口
public interface IRedPacketService {
    /*
    发红包业务处理
     */
    String handOut(RedPacketDto dto) throws Exception;
    
    /*
    抢红包业务
     */
    BigDecimal rob(Integer userId,String redId) throws Exception;
}
