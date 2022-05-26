package com.example.demo.controller;

import com.example.demo.domain.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: hujun
 * @date: 2021/03/05  15:55
 */
@Controller
public class MyThymeleafController {
    @GetMapping("/getStudents")
    public ModelAndView getStudent() {
        List<Student> students = new LinkedList<>();
        Student student = new Student();
        student.setId(1);
        student.setName("全栈学习笔记");
       // student.setAge(21);
        Student student1 = new Student();
        student1.setId(2);
        student1.setName("张三");
       // student1.setAge(22);
        students.add(student);
        students.add(student1);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("students", students);
        modelAndView.setViewName("/src/main/resources/static/students.html");
        return modelAndView;
    }
}
