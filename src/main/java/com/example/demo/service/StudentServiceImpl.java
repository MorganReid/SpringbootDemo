package com.example.demo.service;

import com.example.demo.domain.Student;
import org.springframework.stereotype.Service;

/**
 * @author: hujun
 * @date: 2022/07/05  17:35
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public Student selectOne(Integer id) {
        return null;
    }

    @Override
    public void print() {
        System.out.println("service student");
    }

    @Override
    public void printStu(Student student) {
        System.out.println(student.getName());
    }
}
