package com.hui.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="book")
public class BookConfigBean {

    private String author;
    private String value;
    private int intValue;
    private String uuid;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "BookConfigBean{" +
                "author='" + author + '\'' +
                ", value='" + value + '\'' +
                ", intValue=" + intValue +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
