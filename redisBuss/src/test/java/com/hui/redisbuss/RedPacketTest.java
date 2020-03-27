package com.hui.redisbuss;

import com.hui.redisbuss.tool.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class RedPacketTest {
    
    @Test
    public void one() {
        Integer amout = 1000;
        Integer total = 10;
        List<Integer> list = RedPacketUtil.divideRedPackage(amout,total);
        log.info("总金额={} 分,总个数={} 个",amout,total);
        Integer sum = 0;
        for(Integer i : list) {
            log.info("随机金额为：{}分，即 {} 元",i,new BigDecimal(i.toString()).divide(new BigDecimal(100)));
            sum = sum +i;
        }
        
        log.info("所有红包和为：{}",sum);
    
    }
}
