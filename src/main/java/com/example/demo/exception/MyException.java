package com.example.demo.exception;

import lombok.Data;

/**
 * @author: hujun
 * @date: 2021/01/14  18:58
 */
@Data
public class MyException extends RuntimeException {
    private int code;
    private String message;

    public MyException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
