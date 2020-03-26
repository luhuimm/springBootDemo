package com.hui.boottest;

import com.hui.boottest.service.UnitTestService;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//SpringBootTest 是springboot 用于测试的注解，
// 可指定启动类或者测试环境等，这里直接默认。
@SpringBootTest
@RunWith(SpringRunner.class)
public class UnitTestServiceTest {
    
    
    @Autowired
    UnitTestService testService;
    
    //引入 ContiPerf 进行性能测试
    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();
    
    @Test
    //10个线程 执行10次
    @PerfTest(invocations = 100,threads = 10)
    public void test() {
        String msg = "this is a test";
        String result = testService.process(msg);
        //断言 是否和预期一致
        Assert.assertEquals(msg, result);
    }
}
