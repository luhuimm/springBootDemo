package com.hui.webbasic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
@ServletComponentScan
@Slf4j
public class WebbasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebbasicApplication.class, args);
        log.info("服务启动");
    }
    
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //当过滤器有注入其他bean类时，可直接通过@bean的方式进行实体类过滤器，这样不可自动注入过滤器使用的其他bean类。
        //当然，若无其他bean需要获取时，可直接new CustomFilter()，也可使用getBean的方式。
        registration.setFilter(customFilter());
        //过滤器名称
        registration.setName("customFilter");
        //拦截路径
        registration.addUrlPatterns("/*");
        //设置顺序
        registration.setOrder(10);
        return registration;
    }
    
    @Bean
    public Filter customFilter() {
        return new CustomFilter();
    }

}
