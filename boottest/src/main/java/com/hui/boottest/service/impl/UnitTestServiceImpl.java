package com.hui.boottest.service.impl;

import com.hui.boottest.service.UnitTestService;
import org.springframework.stereotype.Service;

@Service
public class UnitTestServiceImpl implements UnitTestService {
    @Override
    public String process(String msg) {
        return msg;
    }
}
