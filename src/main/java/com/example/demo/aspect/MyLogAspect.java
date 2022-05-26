package com.example.demo.aspect;

import com.example.demo.annotation.MyLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author: hujun
 * @date: 2021/03/06  10:51
 */
@Aspect
@Component
@Slf4j
public class MyLogAspect {

    @Pointcut("@annotation(com.example.demo.annotation.MyLog)")
    public void myPointCut() {
        //签名，可以理解成这个切入点的一个名称
    }

    @Before("myPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取url,请求方法，ip地址，类名以及方法名，参数
        log.info("url={},method={},ip={},class_method={},args={}", request.getRequestURI(), request.getMethod(), request.getRemoteAddr(), joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), joinPoint.getArgs());

    }

    @AfterReturning(pointcut = "myPointCut()")
    public void printLog(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MyLog myLog = method.getAnnotation(MyLog.class);
        String value = null;
        if (myLog != null) {
          //  value = myLog.value();
        }
        log.info(new Date() + "-----" + value);
    }
}
