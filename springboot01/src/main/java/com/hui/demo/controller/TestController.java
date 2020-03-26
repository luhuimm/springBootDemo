package com.hui.demo.controller;

import com.hui.demo.BookConfigBean;
import com.hui.demo.ConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private BookConfigBean bookConfigBean;

    @Autowired
    private ConfigBean configBean;

    @Value("${book.value}")
    private String bookValue;

    @Value("${book.intValue}")
    private int bookIntValue;

    @Value("${book.uuid}")
    private String bookUuid;

    @GetMapping("/test1")
    public String test1() {

        return "book.value = " + bookValue + " book.intValue="+bookIntValue
                + " book.uuid=" + bookUuid;
    }

    @GetMapping("/test2")
    public BookConfigBean test2() {
       return bookConfigBean;
    }

    @GetMapping("/test3")
    public ConfigBean test3() {

        return configBean;
    }
}
