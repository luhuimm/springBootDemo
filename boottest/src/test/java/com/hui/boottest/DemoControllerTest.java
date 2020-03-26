package com.hui.boottest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerTest {
    //使用 WebMvcTest 时使用
    //@autowired mockMvc 是可自动注入的。
    //当直接使用SpringBootTest 会提示 注入失败  这里直接示例利用 MockMvcBuilders工具创建
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testDemo() throws Exception {
        String msg = "this is a mock test";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                get("/mock").param("msg",msg)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        //断言 是否和预期相等
        Assert.assertEquals(msg, result.getResponse().getContentAsString());
        
    }
}

