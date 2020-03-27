package com.hui.redisdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhoneUser implements Serializable {
    private String phone;
    private Double fare;
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneUser phoneUser = (PhoneUser) o;
        return phone.equals(phoneUser.phone);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}
