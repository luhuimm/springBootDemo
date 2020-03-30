package com.hui.lockdemo.controller;

import com.hui.lockdemo.common.BaseResponse;
import com.hui.lockdemo.common.StatusCode;
import com.hui.lockdemo.service.DataBaseLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DataBaseLockController {
    
    private static final String prefix = "db";
    
    @Autowired
    private DataBaseLockService dataBaseLockService;
    
    @GetMapping(value = prefix+"/money/take")
    public BaseResponse takeMoney(Integer userId, Double amount) {
        if (amount == null || userId == null) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            dataBaseLockService.takeMoney(userId,amount);
        } catch (Exception e) {
            log.info("操作失败:{}",e.getMessage());
            response = new BaseResponse(StatusCode.Fail);
        }
        return response;
    }
    
    @GetMapping(value = prefix+"/money/lock/take")
    public BaseResponse takeLockMoney(Integer userId, Double amount) {
        
        if (amount == null || userId == null) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
    
        try {
            dataBaseLockService.takeMoneyWithLock(userId,amount);
        } catch (Exception e) {
            log.info("操作失败:{}",e.getMessage());
            response = new BaseResponse(StatusCode.Fail);
            return response;
        }
        return response;
    }
}
