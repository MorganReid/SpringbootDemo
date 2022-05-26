package com.example.demo.controller;

import com.example.demo.annotation.MyLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hujun
 * @date: 2021/03/06  10:51
 */
@RestController
public class MyLogController {

    @GetMapping("/testLog")
    public void testLog() {
        System.out.println("自定义日志注解");
    }
}
