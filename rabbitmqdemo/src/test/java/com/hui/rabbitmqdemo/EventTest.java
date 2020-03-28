package com.hui.rabbitmqdemo;

import com.hui.rabbitmqdemo.event.Publisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EventTest {

    @Autowired
    private Publisher publisher;
    
    @Test
    public void test1(){
        publisher.sendMsg();
    }
}
