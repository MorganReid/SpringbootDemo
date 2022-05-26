package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hujun
 * @date: 2021/03/04  19:09
 */
@RestController
@Slf4j
public class MyFilterController {

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String hello() {
        log.info("controller服务");
        return "TestController" + System.currentTimeMillis();
    }

}
