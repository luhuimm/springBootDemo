package com.hui.redisbuss.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

// 发红包记录
@Data
public class RedRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String redPacket;
    private Integer total;
    private BigDecimal amount;
    private Byte isActive;
    private Date createTime;
}
