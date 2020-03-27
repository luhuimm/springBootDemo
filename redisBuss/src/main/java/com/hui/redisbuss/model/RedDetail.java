package com.hui.redisbuss.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RedDetail {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer recordId;
    private BigDecimal amount;
    private Byte isActive;
    private Date createTime;
}
