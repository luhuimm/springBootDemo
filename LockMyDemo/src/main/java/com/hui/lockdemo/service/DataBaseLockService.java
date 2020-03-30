package com.hui.lockdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hui.lockdemo.entity.UserAccount;
import com.hui.lockdemo.entity.UserAccountRecord;
import com.hui.lockdemo.mapper.UserAccountMapper;
import com.hui.lockdemo.mapper.UserAccountRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class DataBaseLockService {
    
    @Autowired
    private UserAccountMapper userAccountMapper;
    
    @Autowired
    private UserAccountRecordMapper userAccountRecordMapper;
    
    public void takeMoney(Integer userId, Double amount) throws Exception {
    
        // 获取当前账号的信息
         UserAccount userAccount = userAccountMapper.
                 selectOne(new QueryWrapper<UserAccount>().eq("user_id",userId));
        // 是否有足够的金额来提现
         if(userAccount != null && userAccount.getAmount().doubleValue() -amount > 0) {
             // 把提前的金额，从用户的 amount 中减去。
             userAccount.setAmount(BigDecimal.valueOf( userAccount.getAmount().doubleValue() - amount));
             Thread.sleep(200);
             //主键id 来更新
             userAccountMapper.updateById(userAccount);
             // 插入提取现金的历史记录
             UserAccountRecord userAccountRecord = new UserAccountRecord();
             userAccountRecord.setCreateTime(new Date());
             userAccountRecord.setAccountId(userAccount.getId());
             userAccountRecord.setMoney(BigDecimal.valueOf(amount));
             userAccountRecordMapper.insert(userAccountRecord);
             log.info("当前待提现的金额为:{}，用户账号余额为：{}",amount,userAccount.getAmount());
         } else {
             
             throw  new Exception("账号余额不足，或账号不存在");
         }
     
     }
     
     
     public void takeMoneyWithLock(Integer userId, Double amount) throws Exception {
    
        // 获取当前账号的信息
         UserAccount userAccount = userAccountMapper.
                 selectOne(new QueryWrapper<UserAccount>().eq("user_id",userId));
         // 是否有足够的金额来提现
         if(userAccount != null && userAccount.getAmount().doubleValue() - amount > 0) {
             Thread.sleep(200);
             // 构造更新条件，根据查到的版本号，和 主键id 来更新
             QueryWrapper<UserAccount> queryWrapper = new QueryWrapper<>();
             queryWrapper.eq("version", userAccount.getVersion()).eq("id",userAccount.getId());
             //减去提取的金额
             userAccount.setAmount(BigDecimal.valueOf( userAccount.getAmount().doubleValue() - amount));
             //版本号加1
             userAccount.setVersion(userAccount.getVersion() +1);
             // 进行更新操作
             int res = userAccountMapper.update(userAccount, queryWrapper);
             // 更新成功后的操作
             if( res > 0 ){
                 // 插入提取现金的历史记录
                 UserAccountRecord userAccountRecord = new UserAccountRecord();
                 userAccountRecord.setCreateTime(new Date());
                 userAccountRecord.setAccountId(userAccount.getId());
                 userAccountRecord.setMoney(BigDecimal.valueOf(amount));
                 userAccountRecordMapper.insert(userAccountRecord);
                 log.info("当前待提现的金额为:{}，用户账号余额为：{}",amount,userAccount.getAmount());
             } else {
                 throw  new Exception("提现失败---请后续重试");
             }
         } else {
             throw  new Exception("账号余额不足，或账号不存在");
         }
         
     }
     
}
