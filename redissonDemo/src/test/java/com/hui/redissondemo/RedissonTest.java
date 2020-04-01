package com.hui.redissondemo;


import com.hui.redissondemo.entity.BloomDto;
import com.hui.redissondemo.entity.RMapDto;
import com.hui.redissondemo.entity.RSetDto;
import com.hui.redissondemo.entity.UserLoginDto;
import com.hui.redissondemo.message.UserLoginPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;
    
    @Autowired
    private UserLoginPublisher userLoginPublisher;
    
    @Test
    public void test1() throws IOException {
        log.info("redisson的配置信息：{}",redissonClient.getConfig().toJSON());
    }
    @Test
    public void test2() {
        // 定义redis 中的 key
        final String key = "myBloomFilterDataV2";
        // 初始化构造数组容量大小
        Long total = 100000L;
        // 创造布隆过滤器组件
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter(key);
        // 初始化布隆过滤器，预统计元素数量为100000，期望误差率为 0.01
        bloomFilter.tryInit(total,0.01);
        // 往过滤器中添加元素
        for(int i = 1 ; i < total; i++) {
            bloomFilter.add(i);
        }
        log.info("该布隆过滤器中是否包含数据1：{}",bloomFilter.contains(1));
        log.info("该布隆过滤器中是否包含数据 -1：{}",bloomFilter.contains(-1));
        log.info("该布隆过滤器中是否包含数据10000：{}",bloomFilter.contains(10000));
        log.info("该布隆过滤器中是否包含数据1000000：{}",bloomFilter.contains(1000000));
    }
    
    @Test
    public void test3() {
        // 定义redis 中的 key
        final String key = "myBloomFilterDataV4";
        // 创造布隆过滤器组件
        RBloomFilter<BloomDto> bloomFilter = redissonClient.getBloomFilter(key);
        // 初始化布隆过滤器，预统计元素数量为100000，期望误差率为 0.01
        bloomFilter.tryInit(1000,0.01);
        // 往过滤器中添加元素
        BloomDto dto1 = new BloomDto(1,"1");
        BloomDto dto2 = new BloomDto(10,"10");
        BloomDto dto3 = new BloomDto(100,"100");
        BloomDto dto4 = new BloomDto(1000,"1000");
        BloomDto dto5 = new BloomDto(10000,"10000");
        bloomFilter.add(dto1);
        bloomFilter.add(dto2);
        bloomFilter.add(dto3);
        bloomFilter.add(dto4);
        bloomFilter.add(dto5);
        
        log.info("该布隆过滤器中是否包含数据(1,1)：{}",bloomFilter.contains(new BloomDto(1,"1")));
        log.info("该布隆过滤器中是否包含数据（100,2）：{}",bloomFilter.contains(new BloomDto(100,"2")));
        log.info("该布隆过滤器中是否包含数据（1000,3）：{}",bloomFilter.contains(new BloomDto(1000,"3")));
        log.info("该布隆过滤器中是否包含数据（10000,10000）：{}",bloomFilter.contains(new BloomDto(10000,"10000")));
    }
    
    @Test
    public void test4() {
        //创建用户登录成功后的实体对象
        UserLoginDto dto = new UserLoginDto();
        // 设置用户id,用户名及密码等信息
        dto.setUserId(1001);
        dto.setPassword("hui");
        dto.setUserName("miao");
        
        userLoginPublisher.sendMsg(dto);
    
    }
    
    @Test
    public void mapTest() {
        final String key = "myRidissonMap";
        RMapDto dto1 = new RMapDto(1,"map1");
        RMapDto dto2 = new RMapDto(2,"map2");
        RMapDto dto3 = new RMapDto(3,"map3");
        RMapDto dto4 = new RMapDto(4,"map4");
        RMapDto dto5 = new RMapDto(5,"map5");
        RMapDto dto6 = new RMapDto(6,"map6");
        RMapDto dto7 = new RMapDto(7,"map7");
        RMapDto dto8 = new RMapDto(8,"map8");
        //获取RMap功能组件实例
        RMap<Integer,RMapDto> rMap = redissonClient.getMap(key);
        // 正常的添加元素
        rMap.put(dto1.getId(),dto1);
        // 异步方式添加元素
        rMap.putAsync(dto2.getId(),dto2);
        // 添加元素之前判断是否存在，如果不存在才添加，否则不添加
        rMap.putIfAbsent(dto3.getId(),dto3);
        // 添加元素之前判断是否存在，如果不存在才添加，否则不添加 异步方式
        rMap.putIfAbsentAsync(dto4.getId(),dto4);
        // 正常添加元素，快速方式
        rMap.fastPut(dto5.getId(),dto5);
        // 正常添加元素，快速方式,如果不存在才添加，否则不添加
        rMap.fastPutIfAbsent(dto6.getId(),dto6);
        //  正常添加元素，快速方式, 异步
        rMap.fastPutAsync(dto7.getId(),dto7);
        //  正常添加元素，快速方式,如果不存在才添加，否则不添加 异步
        rMap.fastPutIfAbsentAsync(dto8.getId(),dto8);
    }
    
    @Test
    public void test6() {
        final String key = "myRidissonMap";
        RMap<Integer,RMapDto> rMap = redissonClient.getMap(key);
        Set<Integer> ids = rMap.keySet();
        Map<Integer,RMapDto> map = rMap.getAll(ids);
        log.info("元素列表:{}",map);
        // 根据id来移除
        rMap.remove(6);
        map = rMap.getAll(rMap.keySet());
        log.info("移除后的，元素列表:{}",map);
        final Integer[] removeIds = new Integer[]{1,2,3};
        rMap.fastRemove(removeIds);
        map = rMap.getAll(rMap.keySet());
        log.info("移除后的--，元素列表:{}",map);
    }
    
    /**
     * 元素淘汰机制
     */
    @Test
    public void test7() throws InterruptedException {
        final String key = "myRidissonMapCache";
        RMapCache<Integer,RMapDto> rmap = redissonClient.getMapCache(key);
        RMapDto dto1 = new RMapDto(1,"map1");
        RMapDto dto2 = new RMapDto(2,"map2");
        RMapDto dto3 = new RMapDto(3,"map3");
        RMapDto dto4 = new RMapDto(4,"map4");
        rmap.put(dto1.getId(),dto1);
        // 元素存活时间 是 10s
        rmap.putIfAbsent(dto2.getId(),dto2,10, TimeUnit.SECONDS);
        rmap.put(dto3.getId(),dto3);
        // 元素存活时间 是 5s
        rmap.putIfAbsent(dto4.getId(),dto4,5, TimeUnit.SECONDS);
        Map<Integer,RMapDto> map = rmap.getAll(rmap.keySet());
        log.info("元素列表:{}",map);
        Thread.sleep(5000);
        map = rmap.getAll(rmap.keySet());
        log.info("等待5s后 元素列表:{}",map);
        Thread.sleep(5000);
        map = rmap.getAll(rmap.keySet());
        log.info("等待10s后 元素列表:{}",map);
    }
    
    @Test
    public void test8() {
        final String key = "myRedissonSortedSetV2";
        RSetDto rSetDto1 = new RSetDto(1,"N1",20,10.0D);
        RSetDto rSetDto2 = new RSetDto(2,"N2",18,2.0D);
        RSetDto rSetDto3 = new RSetDto(3,"N3",21,8.0D);
        RSetDto rSetDto4 = new RSetDto(4,"N4",19,6.0D);
        RSetDto rSetDto5 = new RSetDto(5,"N5",22,1.0D);
        RSortedSet<RSetDto> rSetDtos = redissonClient.getSortedSet(key);
        rSetDtos.trySetComparator(new Comparator<RSetDto>() {
            // 根据年龄来进行比较
            @Override
            public int compare(RSetDto o1, RSetDto o2) {
                return o1.getAge().compareTo(o2.getAge());
            }
        });
        rSetDtos.add(rSetDto1);
        rSetDtos.add(rSetDto2);
        rSetDtos.add(rSetDto3);
        rSetDtos.add(rSetDto4);
        rSetDtos.add(rSetDto5);
        Collection<RSetDto> result = rSetDtos.readAll();
        log.info("此时有序集合Set元素列表:{}",result);
    }
    
    @Test
    public void test9() {
        final String key = "myRedissonSortedSet";
        RSetDto rSetDto1 = new RSetDto(1,"N1",20,10.0D);
        RSetDto rSetDto2 = new RSetDto(2,"N2",18,2.0D);
        RSetDto rSetDto3 = new RSetDto(3,"N3",21,8.0D);
        RSetDto rSetDto4 = new RSetDto(3,"N4",21,6.0D);
        RScoredSortedSet<RSetDto> rSetDtos = redissonClient.getScoredSortedSet(key);
        rSetDtos.add(rSetDto1.getScore(),rSetDto1);
        rSetDtos.add(rSetDto2.getScore(),rSetDto2);
        rSetDtos.add(rSetDto3.getScore(),rSetDto3);
        rSetDtos.add(rSetDto4.getScore(),rSetDto4);
        // 通过SortOrder指定读取出的元素的正序还是倒序
        Collection<RSetDto> result = rSetDtos.readSortAlpha(SortOrder.DESC);
        log.info("此时得分排序集合SocredSortedSet 的元素列表--从大到小{}",result);
        // 获取对象元素在集合中的位置--相当于获取排名
        log.info("获取对象元素的排名：对象元素={}，从大到小排名={}",rSetDto1,rSetDtos.revRank(rSetDto1)+1);
        log.info("获取对象元素的排名：对象元素={}，从大到小排名={}",rSetDto2,rSetDtos.revRank(rSetDto2)+1);
        log.info("获取对象元素的排名：对象元素={}，从大到小排名={}",rSetDto3,rSetDtos.revRank(rSetDto3)+1);
        log.info("获取对象元素的排名：对象元素={}，从大到小排名={}",rSetDto4,rSetDtos.revRank(rSetDto4)+1);
    }
    
}
