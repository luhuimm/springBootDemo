package com.hui.redissondemo.entity;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class BloomDto implements Serializable {
    private Integer id ;
    private String msg;
}
