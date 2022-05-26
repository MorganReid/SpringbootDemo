package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hujun
 * @date: 2021/03/05  17:13
 */
//@CrossOrigin  表示所有的URL均可访问此资源
//表示只允许这一个url可以跨域访问这个controller
@CrossOrigin(origins = "http://127.0.0.1:8093")
@RestController
@RequestMapping("/testCorss")
public class MyCORSController {

    //可以对方法运用该注解
    //@CrossOrigin(origins = "http://127.0.0.1:8093")
    @GetMapping("/getString")
    public String getString() {
        return "跨域成功！";
    }

}