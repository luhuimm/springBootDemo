package com.hui.rabbitmqdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KnowledgeInfo implements Serializable {
    private Integer id;
    private String mode;
    private String code;
}
