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
public class DeadInfo implements Serializable {
    private Integer id;
    private String msg;
    
}
