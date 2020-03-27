package com.hui.redisdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.hui.redisdemo.mapper.ItemMapper;
import com.hui.redisdemo.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class MybatisplusTest {
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<Item> items = itemMapper.selectList(null);
        items.forEach(System.out::println);
    }
    
    @Test
    public void test2() {

        Item item = itemMapper.selectOne(new QueryWrapper<Item>().lambda()
                .eq(Item::getCode,"book_10010"));
        
        log.info("item={}",item);
    }
}
