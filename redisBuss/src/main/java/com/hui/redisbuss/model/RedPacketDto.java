package com.hui.redisbuss.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

// 发红包是请求对象的接收参数
@Data
@ToString
public class RedPacketDto {
   
    private Integer userId;
    @NotNull
    private Integer total;
    @NotNull
    private Integer amount;
}
