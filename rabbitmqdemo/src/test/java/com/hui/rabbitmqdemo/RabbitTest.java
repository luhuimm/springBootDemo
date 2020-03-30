package com.hui.rabbitmqdemo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hui.rabbitmqdemo.entity.DeadInfo;
import com.hui.rabbitmqdemo.entity.EventInfo;
import com.hui.rabbitmqdemo.entity.KnowledgeInfo;
import com.hui.rabbitmqdemo.entity.Person;
import com.hui.rabbitmqdemo.rabbitmq.dead.DeadPublisher;
import com.hui.rabbitmqdemo.rabbitmq.direct.DirectPublisher;
import com.hui.rabbitmqdemo.rabbitmq.fanout.ModelPublisher;
import com.hui.rabbitmqdemo.rabbitmq.knowledge.AutoKnowledgePublisher;
import com.hui.rabbitmqdemo.rabbitmq.knowledge.ManualKnowledgePublisher;
import com.hui.rabbitmqdemo.rabbitmq.publisher.BasicPublisher;
import com.hui.rabbitmqdemo.rabbitmq.topic.TopicPublisher;
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
    
    @Autowired
    private ModelPublisher modelPublisher;
    
    @Autowired
    private DirectPublisher directPublisher;
    
    @Autowired
    private TopicPublisher topicPublisher;
    
    @Autowired
    private AutoKnowledgePublisher autoKnowledgePublisher;
    
    @Autowired
    private ManualKnowledgePublisher manualKnowledgePublisher;
    
    @Autowired
    private DeadPublisher deadPublisher;
    
    @Test
    public void test1() {
        basicPublisher.sendMsg("这是第一个测试代码");
    }
    
    @Test
    public void test2() {
        Person p = new Person(1,"大神","debug");
        basicPublisher.sendObjMsg(p);
    }
    
    @Test
    public void fanoutTest() {
        EventInfo eventInfo = new EventInfo(1,"login模块","基于fanoutExchange的消息",
                "fanoutExchange的消息模型");
        modelPublisher.sendObjMsg(eventInfo);
    
    }
    
    @Test
    public void DirectTest() {
        EventInfo eventInfo = new EventInfo(1,"login模块","基于DirectExchange的消息",
                "fanoutExchange的消息模型");
        directPublisher.sendMsgDirectOne(eventInfo);
    
        eventInfo = new EventInfo(2,"logout模块","基于DirectExchange的消息",
                "DirectExchange的消息模型");
    
        directPublisher.sendMsgDirectTwo(eventInfo);
        
    }
    
    @Test
    public void topicTest() {
        String msg = "TopicExchange的消息模型";
        //#标识一个任意字符java
        String routeKeyOne = "topic.java.key";
        //#相当于php.python
        String routeKeyTwo = "topic.php.python.key";
        //#相当于 0 个单词
        String routeKeyThree = "topic.key";
    
        topicPublisher.sendMsgTopic(msg,routeKeyThree);
    }
    
    @Test
    public void autoConfirmTest() throws JsonProcessingException {
        EventInfo eventInfo = new EventInfo(1,"login模块","基于DirectExchange的消息",
                "fanoutExchange的消息模型");
        autoKnowledgePublisher.sendAutoMsg(eventInfo);
    }
    
    @Test
    public void autoManualTest() throws JsonProcessingException {
        EventInfo eventInfo = new EventInfo(1,"login模块","基于DirectExchange的消息",
                "fanoutExchange的消息模型");
        manualKnowledgePublisher.sendManualMsg(eventInfo);
    }
    
    @Test
    public void deadTest() throws InterruptedException, JsonProcessingException {
        DeadInfo info = new DeadInfo(1,"~~~我是第一则消息~~");
        deadPublisher.sendMsg(info);
    
        info =  new DeadInfo(2,"~~~我是第二则消息~~");
        deadPublisher.sendMsg(info);
        // 等待30s
        Thread.sleep(30000);
    }
    
}
