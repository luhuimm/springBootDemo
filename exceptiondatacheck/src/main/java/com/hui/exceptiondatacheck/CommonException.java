package com.hui.exceptiondatacheck;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonException extends Exception {
    
    private String code;
    private String msg;
}
