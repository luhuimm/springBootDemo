package com.hui.rabbitmqdemo;


import com.hui.rabbitmqdemo.entity.Person;
import com.hui.rabbitmqdemo.rabbitmq.publisher.BasicPublisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitTest {
    
    @Autowired
    private BasicPublisher basicPublisher;
    
    @Test
    public void test1() {
        basicPublisher.sendMsg("这是第一个测试代码");
    }
    
    @Test
    public void test2() {
        Person p = new Person(1,"大神","debug");
        basicPublisher.sendObjMsg(p);
    }
}
