package com.hui.lockdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountRecord {
    @TableId(type = IdType.AUTO) // 主键数据库自增
    private Integer id;
    private Integer accountId;
    private BigDecimal money;
    private Date createTime;
}
