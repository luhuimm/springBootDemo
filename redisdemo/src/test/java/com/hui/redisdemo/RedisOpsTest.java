package com.hui.redisdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hui.redisdemo.model.Fruit;
import com.hui.redisdemo.model.Person;
import com.hui.redisdemo.model.PhoneUser;
import com.hui.redisdemo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisOpsTest {
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    // 定义json 序列化和反序列化框架类
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void one() throws JsonProcessingException {
        Person person = new Person("001",23,"huige","大渺","罗田");
        final String key = "redis:person:001";
        final String value = objectMapper.writeValueAsString(person);
        redisTemplate.opsForValue().set(key,value);
        Object result = redisTemplate.opsForValue().get(key);
        if(result != null) {
            Person resP= objectMapper.readValue(result.toString(),Person.class);
            log.info("从缓存中读出来反序列化的数据是:{}",resP);
        }
    }
    
    @Test
    public void two(){
        List<Person> list = new ArrayList<>();
        list.add(new Person("001",23,"damiao","大渺","罗田"));
        list.add(new Person("002",24,"huige","辉哥","黄梅"));
        list.add(new Person("003",25,"张三","zhangsan","浠水"));
        list.add(new Person("004",26,"李四","lisi","武汉"));
        log.info("构造好了已排序的列表:{}",list);
        final String key = "redis:list:2";
        ListOperations listOperations = redisTemplate.opsForList();
        for(Person p : list) {
            //往列表中添加---从尾部添加
            listOperations.leftPush(key,p);
        }
        // 获取数据
        log.info("获取list的数据，从队头获取--");
        Object res = listOperations.rightPop(key);
        Person resP ;
        while (res != null) {
            resP = (Person) res;
            log.info("当前读到是数据是：{}",resP);
            res = listOperations.rightPop(key);
        }
    }
    
    @Test
    public void three(){
        List<String> userList = new ArrayList<>();
        userList.add("hege");
        userList.add("hege");
        userList.add("debug");
        userList.add("haha");
        userList.add("Debug");
        log.info("待处理的用户列表:{}",userList);
        final String key = "redis:set";
        SetOperations setOperations = redisTemplate.opsForSet();
        for(String user: userList) {
            setOperations.add(key,user);
        }
        log.info("从缓存中获取数据--");
        Object res = setOperations.pop(key);
        while (res != null){
            log.info("从缓存中获取的数据--当前数据：{}",res);
            res = setOperations.pop(key);
        }
    }
    
    @Test
    public void four(){
        List<PhoneUser> list = new ArrayList<>();
        final String key = "redis:test:4";
        list.add(new PhoneUser("103",130.0));
        list.add(new PhoneUser("101",120.0));
        list.add(new PhoneUser("102",80.0));
        list.add(new PhoneUser("105",70.0));
        list.add(new PhoneUser("106",50.0));
        list.add(new PhoneUser("104",150.0));
        log.info("构造一组无序的充值列表:{}",list);
        //操作前先清空
        redisTemplate.delete(key);
    
        ZSetOperations zSetOperations =  redisTemplate.opsForZSet();
        for(PhoneUser phoneUser : list) {
            zSetOperations.add(key,phoneUser,phoneUser.getFare());
        }
        //从前端获取访问充值排名靠前的用户列表
        Long size = zSetOperations.size(key);
        // 从小到大排序
        Set<PhoneUser> resSet =  zSetOperations.range(key,0L,size);
        // 从大到小排序
//       Set<PhoneUser> resSet =  zSetOperations.reverseRange(key,0L,size);
        //遍历获取有序集合中的元素
        for(PhoneUser phoneUser: resSet) {
            log.info("从缓存中读取手机充值记录排序列表，当前记录为:{}",phoneUser);
        }
        
    }
    
    @Test
    public void five(){
        List<Student> students = new ArrayList<>();
        List<Fruit> fruits = new ArrayList<>();
        students.add(new Student("10010","debug","大神"));
        students.add(new Student("10011","jack","修罗"));
        students.add(new Student("10012","sam","上古"));
        
        fruits.add(new Fruit("apple","红色"));
        fruits.add(new Fruit("banana","橙色"));
        fruits.add(new Fruit("orange","黄色"));
        
        final String skey = "redis:studet";
        final String fkey = "redis:fruit";
    
        // 存储数据到缓存中
        HashOperations hashOperations = redisTemplate.opsForHash();
        for (Student s : students) {
            hashOperations.put(skey,s.getId(),s);
        }
        for (Fruit fruit : fruits) {
            hashOperations.put(fkey,fruit.getName(),fruit);
        }
        
        // 获取学生对象列表和水果对象列表
        Map<String,Student> stringStudentMap = hashOperations.entries(skey);
        log.info("获取学生对象列表:{}",stringStudentMap);
        
        Map<String,Fruit> fruitMap = hashOperations.entries(fkey);
        log.info("获取水果对象列表:{}",fruitMap);
        // 获取指定学生对象
        String sfiled = "10012";
        Student s = (Student) hashOperations.get(skey,sfiled);
        log.info("学生对象：{}",s);
    }
    
    // key 失效
    @Test
    public void six() throws InterruptedException {
        
        final String key ="redis:test:6";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 第一种方法，在缓存中，set数据时，提供一个TTL,表示ttl 时间一到，缓存中的key
        // 将自动失效，即被清理， 在这ttl 是 10s
        valueOperations.set(key,"expire操作",10L, TimeUnit.SECONDS);
        // 等待5s  判断key 是否存在
        Thread.sleep(5);
        
        Boolean existKey = redisTemplate.hasKey(key);
        Object v = valueOperations.get(key);
        log.info("等待5s-判断key 是否存在：{},对应值为：{}",existKey,v);
        // 再等待10s
        Thread.sleep(10);
    
        existKey = redisTemplate.hasKey(key);
        v = valueOperations.get(key);
        log.info("等待15s-判断key 是否存在：{},对应值为：{}",existKey,v);
    }
    
    @Test
    public void seven() throws InterruptedException {
        
        final String key ="redis:test:7";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 第一种方法，在缓存中，set数据时，提供一个TTL,表示ttl 时间一到，缓存中的key
        // 将自动失效，即被清理， 在这ttl 是 10s
        
        valueOperations.set(key,"expire操作2");
        redisTemplate.expire(key,10L,TimeUnit.SECONDS);
        
        // 等待5s  判断key 是否存在
        Thread.sleep(5000);
        
        Boolean existKey = redisTemplate.hasKey(key);
        Object v = valueOperations.get(key);
        log.info("等待5s-判断key 是否存在：{},对应值为：{}",existKey,v);
        // 再等待10s
        Thread.sleep(5000);
        
        existKey = redisTemplate.hasKey(key);
        v = valueOperations.get(key);
        log.info("等待10s-判断key 是否存在：{},对应值为：{}",existKey,v);
    }
}


