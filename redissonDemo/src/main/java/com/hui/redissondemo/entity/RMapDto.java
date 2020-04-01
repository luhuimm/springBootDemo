package com.hui.redissondemo.entity;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RMapDto implements Serializable {
    private Integer id;
    private String name;
}
