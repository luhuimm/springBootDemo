package com.hui.redissondemo.entity;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString
public class UserLoginDto implements Serializable {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    
    private Integer userId;
}
