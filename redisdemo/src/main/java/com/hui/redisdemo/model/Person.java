package com.hui.redisdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Person implements Serializable {
    private String id;
    private Integer age;
    private String name;
    private String userName;
    private String address;
}
