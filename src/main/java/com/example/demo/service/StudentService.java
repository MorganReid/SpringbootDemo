package com.example.demo.service;

import com.example.demo.domain.Student;

/**
 * @author: hujun
 * @date: 2022/07/05  17:33
 */
public interface StudentService {


    Student selectOne(Integer id);

    void print();

    void printStu(Student student);
}
