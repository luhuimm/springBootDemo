package com.hui.redissondemo.entity;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RSetDto implements Serializable {
    private Integer id;
    private String name;
    private Integer age;
    private Double score;
}
