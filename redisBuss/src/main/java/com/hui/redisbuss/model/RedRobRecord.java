package com.hui.redisbuss.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RedRobRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String redPacket;
    private BigDecimal amount;
    private Byte isActive;
    private Date createTime;
}
