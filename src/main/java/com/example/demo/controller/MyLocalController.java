package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hujun
 * @date: 2021/03/09  16:21
 */
@RestController
public class MyLocalController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/testLocal")
    public String testLocal() {
        return messageSource.getMessage("key", null, LocaleContextHolder.getLocale());
    }
}
