package com.hui.demo.model;

import java.io.Serializable;

public class Account  implements Serializable {

    private int id;
    private String name;
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    private String password;

    public Account() {

    }

//    public Account(int id) {
//        this.id = id;
//    }

    public Account(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Account(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
