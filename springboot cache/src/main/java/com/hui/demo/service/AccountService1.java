package com.hui.demo.service;

import com.hui.demo.CacheContext;
import com.hui.demo.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class AccountService1 {

    private final Logger logger = LoggerFactory.getLogger(AccountService1.class);
  
    @Resource
    private CacheContext<Account> accountCacheContext;
  
   public Account getAccountByName(String accountName) {
        Account result = accountCacheContext.get(accountName);
        if (result != null) {
            logger.info("get from cache... {}", accountName);
            return result;
        }
        Optional<Account> accountOptional = getFromDB(accountName);
        if (!accountOptional.isPresent()) {
            throw new IllegalStateException(String.format("can not find account by account name : [%s]", accountName));
        }

        Account account = accountOptional.get();
        accountCacheContext.addOrUpdateCache(accountName, account);
        return account;
    }

    public void reload() {
        accountCacheContext.evictCache();
    }

    private Optional<Account> getFromDB(String accountName) {
        logger.info("real querying db... {}", accountName);
        //Todo query data from database
        return Optional.ofNullable(new Account(accountName));
    }
}
