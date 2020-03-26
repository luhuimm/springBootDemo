package com.hui.exceptiondatacheck;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoReq {
    
    
    @NotBlank(message="code不能为空")
    String code;
    
    @Length(max=10,message="name长度不能超过10")
    String name;
    
    @Constant(message = "verson只能为1.0",value="1.0")
    String version;
}
