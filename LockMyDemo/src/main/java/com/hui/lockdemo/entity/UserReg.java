package com.hui.lockdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserReg {
    @TableId(type = IdType.AUTO) // 主键自增
    private Integer id;
    private String userName;
    private String password;
    private Date createTime;
}
