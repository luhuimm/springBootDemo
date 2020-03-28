package com.hui.rabbitmqdemo.event;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

public class LoginEvent extends ApplicationEvent implements Serializable {
    
    private String userName; // 用户名
    private String loginTime; // 登陆时间
    private String ip; // 所在ip
    
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public LoginEvent(Object source) {
        super(source);
    }
    
    public LoginEvent(Object source, String userName, String loginTime, String ip) {
        super(source);
        this.userName = userName;
        this.loginTime = loginTime;
        this.ip = ip;
    }
    
    @Override
    public String toString() {
        return "LoginEvent{" +
                "userName='" + userName + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
