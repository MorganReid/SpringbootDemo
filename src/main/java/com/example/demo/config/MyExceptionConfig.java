package com.example.demo.config;

import com.example.demo.exception.AjaxCode;
import com.example.demo.exception.AjaxResult;
import com.example.demo.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * @author: hujun
 * @date: 2021/01/14  18:58
 */
@ControllerAdvice
@Slf4j
public class MyExceptionConfig {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResult<Object> defaultException(Exception ex, WebRequest request) {
        log.error("Request has error, request:[{}], msg:[{}]", request.getParameterMap(), ex.getMessage(), ex);
        return AjaxResult.createFailedResult(AjaxCode.SERVER_ERROR, AjaxCode.ERROR_MSG);
    }

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public AjaxResult<Object> myExceptionHandler(MyException ex, WebRequest request) {
        log.error("Request has error, request:[{}], msg:[{}]", request.getParameterMap(), ex.getMessage(), ex);
        return AjaxResult.createFailedResult(ex.getMessage(), ex.getCode(), AjaxCode.ERROR_MSG);
    }

}
