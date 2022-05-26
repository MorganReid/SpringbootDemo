package com.example.demo.interceptor;

import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author: hujun
 * @date: 2021/03/04  20:03
 */
@Configuration
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("过滤器init000");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("过滤器doFilter111");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("过滤器doFilter555");
    }

    @Override
    public void destroy() {
        System.out.println("过滤器destroy666");
    }
}
