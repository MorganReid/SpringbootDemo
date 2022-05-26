package com.example.demo.config;

import com.example.demo.interceptor.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: hujun
 * @date: 2021/03/04  20:07
 */
@Configuration
public class MyFilterConfig {
    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new MyFilter());
        registration.addUrlPatterns("/*");
        registration.setName("myFilter2");
        registration.setOrder(1);
        return registration;
    }
}
