package com.hui.exceptiondatacheck;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController  implements ErrorController {
    
    @GetMapping("/hello")
    public String hello() {
        return  (1/0) + "_";
    }
    
    @GetMapping("/error")
    public Map<String,Object> error() {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("respCode", "9999");
        result.put("respMsg", "error 页面没有找到");
        //正常开发中，可创建一个统一响应实体，如CommonResp
        return result;
    }
    
    @GetMapping("/demo/valid")
    public String demoValid(@Valid DemoReq req) {
        return req.getCode() + "," + req.getName();
    }
    
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
