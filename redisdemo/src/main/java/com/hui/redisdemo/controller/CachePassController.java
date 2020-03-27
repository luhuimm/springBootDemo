package com.hui.redisdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hui.redisdemo.service.CachePassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class CachePassController {


    private static final String prefix = "/cache/pass";
    
    @Autowired
    private CachePassService cachePassService;
    
    @GetMapping(value = prefix+"/item/info")
    public Map<String,Object> getItem(@RequestParam String itemCode) {
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("code",0);
        resMap.put("msg","成功");
        try {
            resMap.put("data", cachePassService.getItemInfo(itemCode));
        } catch (JsonProcessingException e) {
            resMap.put("code",-1);
            resMap.put("msg","失败："+ e.getMessage());
        }
        return resMap;
    }

}
