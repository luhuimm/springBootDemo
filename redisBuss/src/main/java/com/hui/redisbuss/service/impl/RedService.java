package com.hui.redisbuss.service.impl;

import com.hui.redisbuss.mapper.RedDetailMapper;
import com.hui.redisbuss.mapper.RedRecordMapper;
import com.hui.redisbuss.mapper.RedRobRecordMapper;
import com.hui.redisbuss.model.RedDetail;
import com.hui.redisbuss.model.RedPacketDto;
import com.hui.redisbuss.model.RedRecord;
import com.hui.redisbuss.service.IRedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

@Service
@EnableAsync
@Slf4j
public class RedService implements IRedService {
    
    @Autowired
    private RedDetailMapper redDetailMapper;
  
    @Autowired
    private RedRecordMapper redRecordMapper;
    
    @Autowired
    private RedRobRecordMapper redRobRecordMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception {
    
        RedRecord redRecord = new RedRecord();
        redRecord.setUserId(dto.getUserId());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecord.setCreateTime(new Date());
        redRecordMapper.insert(redRecord);
    
        RedDetail redDetail;
        for(Integer i : list) {
            redDetail = new RedDetail();
            redDetail.setRecordId(redRecord.getId());
            redDetail.setAmount(BigDecimal.valueOf(i));
            redDetail.setCreateTime(new Date());
            redDetailMapper.insert(redDetail);
        }
        
    
    
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception {
    
    }
}
