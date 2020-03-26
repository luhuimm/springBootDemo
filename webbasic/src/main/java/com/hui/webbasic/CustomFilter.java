package com.hui.webbasic;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//注册器名称为customFilter,拦截的url为所有
@Slf4j
//@WebFilter(filterName = "customFilter",urlPatterns = {"/*"})
public class CustomFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("filter 初始化");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("doFilter 请求处理");
    
        //链路 直接传给下一个过滤器
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        log.info("filter 销毁");
    }
}
