package com.hui.redisdemo.model;

import lombok.Data;



@Data
public class Item {
    
    private Integer id;
    private String code;
    private String name;
    private java.sql.Timestamp createTime;
    
}
