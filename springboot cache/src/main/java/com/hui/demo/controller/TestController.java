package com.hui.demo.controller;

import com.hui.demo.model.Account;
import com.hui.demo.service.AccountService1;
import com.hui.demo.service.AccountService2;
import com.hui.demo.service.AccountService3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @Autowired
    private AccountService1 accountService1;
    
    @Autowired
    private AccountService2 accountService2;
    
    @Autowired
    private AccountService3 accountService3;
    
    private Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @GetMapping("/test001")
    public String test001() {
        accountService1.getAccountByName("accountName");
        accountService1.getAccountByName("accountName");
    
        accountService1.reload();
        logger.info("after reload ....");
    
        accountService1.getAccountByName("accountName");
        accountService1.getAccountByName("accountName");
        return "ok";
    }
    
    @GetMapping("/test002")
    public String test002() {
        logger.info("first query...");
        accountService2.getAccountByName("accountName");
    
        logger.info("second query...");
        accountService2.getAccountByName("accountName");
        
        return "ok";
    }
    
    
    @GetMapping("/test003")
    public String test003() {
        logger.info("first query.....");
        accountService3.getAccountByName("accountName");
    
        logger.info("second query....");
        accountService3.getAccountByName("accountName");
        
        return "ok";
    }
    
    @GetMapping("/testUpdate")
    public String testUpdate() {
        Account account1 = accountService3.getAccountByName("accountName1");
        logger.info(account1.toString());
        Account account2 = accountService3.getAccountByName("accountName2");
        logger.info(account2.toString());
    
        account2.setId(121212);
        accountService3.updateAccount(account2);
    
        // account1会走缓存
        account1 = accountService3.getAccountByName("accountName1");
        logger.info(account1.toString());
        // account2会查询db
        account2 = accountService3.getAccountByName("accountName2");
        logger.info(account2.toString());
        
        return "ok";
    }
    
    @GetMapping("/testReload")
    public String testReload() {
    
        accountService3.reload();
        // 这2行查询数据库
        accountService3.getAccountByName("somebody1");
        accountService3.getAccountByName("somebody2");
    
        // 这两行走缓存
        accountService3.getAccountByName("somebody1");
        accountService3.getAccountByName("somebody2");
        
        return "ok";
    
    }
    
    @GetMapping("/extend")
    public String extend() {
        Account account = accountService1.getAccountByName("someone");
        logger.info("passwd={}", account.getPassword());
        account = accountService1.getAccountByName("someone");
        logger.info("passwd={}", account.getPassword());
        return "ok";
    }
    
}
