package com.hui.lockdemo.controller;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hui.lockdemo.common.BaseResponse;
import com.hui.lockdemo.common.StatusCode;
import com.hui.lockdemo.service.UserRegService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserRegController {
    
    private static  final String prefix = "/user/reg";
    
    @Autowired
    private UserRegService userRegService;
    
    @GetMapping(value = prefix+"/submit")
    public BaseResponse reg(String userName,String password)  {
     
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
    
        try {
            userRegService.userRegNoLock(userName,password);
        } catch (Exception e) {
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping(value = prefix+"/lock/submit")
    public BaseResponse regWithLock(String userName,String password)  {
        
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        
        try {
            userRegService.userRegWithLock(userName,password);
        } catch (Exception e) {
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
