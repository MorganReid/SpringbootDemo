package com.example.demo.controller;


import com.example.demo.annotation.MyLog;
import com.example.demo.annotation.MyLog2;
import com.example.demo.exception.AjaxResult;
import com.example.demo.exception.ErrorEnum;
import com.example.demo.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

import static org.springframework.util.ReflectionUtils.doWithLocalFields;
import static org.springframework.util.ReflectionUtils.getField;

/**
 * @author: hujun
 * @date: 2021/01/14  18:58
 */
@Slf4j
@RestController
public class MyExceptionController {

    @RequestMapping("test1")
    public String test1() {
        try {
            throw new MyException(1111, "自定义我的异常111");
        } catch (MyException e) {
            log.info("catch 我的异常123  console");
            return "catch MyException  异常信息是：" + e.getMessage();
        }
    }

    @RequestMapping("test2")
    public AjaxResult<String> test2() {
        return AjaxResult.createFailedResult(222, "我的异常22222");
    }

    @RequestMapping("test3")
    public AjaxResult<String> test3() {
        return AjaxResult.createSuccessResult("正常数据");
    }


    @RequestMapping("test4")
    public AjaxResult<String> test4() {

        throw new MyException(ErrorEnum.NO_PERMISSION.getErrorCode(), ErrorEnum.NO_PERMISSION.getErrorMsg());
    }

    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "Welcome to SpringBootDemo!!! [" + new Date() + "]";
    }

    @RequestMapping("/MoreAnno")
    public void testParameterAnnotations(@MyLog @MyLog2 String name,
                                         @MyLog String telephone, Integer age) throws NoSuchMethodException {


        Class<MyExceptionController> clazz = MyExceptionController.class;
        Method method = clazz.getMethod("testParameterAnnotations", String.class, String.class, Integer.class);
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            System.out.println("第" + (i + 1) + "个参数有" + annotations[i].length + "个注解");
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof MyLog) {
                    System.out.println(annotations[i]);
                    handleDataValidationIfForm(annotations[i]);
                } else if (annotations[i][j] instanceof MyLog2) {
                    handleDataValidationIfForm(annotations[i][j]);
                    System.out.println(annotations[i][j]);
                }
            }
        }
    }
    private void handleDataValidationIfForm( Object obj) {
        doWithLocalFields(obj.getClass(), f -> {
            MyLog2 targetAnnotation = AnnotationUtils.getAnnotation(f, MyLog2.class);
            if (targetAnnotation == null) {
                return;
            }

            ReflectionUtils.makeAccessible(f);
            Object filedValue = getField(f, obj);
            if (!(filedValue instanceof Number)) {
                return;
            }
            Long filedLongValue = ((Number) filedValue).longValue();

        });
    }
}
