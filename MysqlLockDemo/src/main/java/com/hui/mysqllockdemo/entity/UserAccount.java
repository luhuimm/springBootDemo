package com.hui.mysqllockdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class UserAccount {
    @TableId(type = IdType.AUTO)  //数据库自增id
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private Integer version;
    private Byte isActive;
}
