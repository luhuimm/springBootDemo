package com.hui.redisbuss.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hui.redisbuss.common.BaseResponse;
import com.hui.redisbuss.common.StatusCode;
import com.hui.redisbuss.model.RedPacketDto;
import com.hui.redisbuss.service.IRedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Slf4j
public class RedPacketController {
    private static final String prefix="/red/packet";
    
    @Autowired
    private IRedPacketService redPacketService;
    
    @PostMapping(value = prefix+"/hand/out",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse handOut(@Validated @RequestBody RedPacketDto dto, BindingResult result) {
    
        if(result.hasErrors()) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try {
            String redId = redPacketService.handOut(dto);
            baseResponse.setData(redId);
        } catch (Exception e) {
            log.error("发红包发生了异常：dto={}",dto,e.fillInStackTrace());
            baseResponse = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return baseResponse;
    }
    
    @GetMapping(value = prefix+"/rob")
    public BaseResponse rob(@RequestParam Integer userId, @RequestParam String redId) {
        BaseResponse response = new BaseResponse(StatusCode.Success);
    
        try {
            BigDecimal result = redPacketService.rob(userId,redId);
            if (result != null) {
                response.setData(result);
            } else {
                response = new BaseResponse(StatusCode.Fail.getCode(), "红包被抢完");
            }
        } catch (Exception e) {
            log.info("抢红包发生了异常：userId={}, redId={}",userId,redId);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}
