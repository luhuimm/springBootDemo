package com.hui.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:test.properties")
@ConfigurationProperties(prefix = "com.book")
public class ConfigBean {
    private String name;
    private String author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
