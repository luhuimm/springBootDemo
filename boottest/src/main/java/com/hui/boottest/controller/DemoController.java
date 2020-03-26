package com.hui.boottest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    
    @GetMapping("/mock")
    public String demo(String msg) {
        return msg;
    }
}
