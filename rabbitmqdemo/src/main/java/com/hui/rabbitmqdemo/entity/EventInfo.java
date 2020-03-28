package com.hui.rabbitmqdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventInfo implements Serializable {

    private Integer id;
    private String module;
    private String name;
    private String desc;
}
