package com.example.demo.controller;

import com.example.demo.domain.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author: hujun
 * @date: 2022/07/05  17:33
 */

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    public String selectOne(Integer id) {
        Student student = studentService.selectOne(id);
        System.out.println("StudentController----" + student.getName());
        return student.getName();
    }

    public void testReturn() {
        studentService.print();
    }

    public void testPrinStu(Student student) {
        studentService.printStu(student);
    }
}
