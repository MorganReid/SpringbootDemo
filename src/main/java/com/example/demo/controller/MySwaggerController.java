package com.example.demo.controller;

import com.example.demo.domain.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hujun
 * @date: 2021/03/05  16:05
 */

@RestController
@Api(tags = "MySwaggerController00000")
public class MySwaggerController {

    @RequestMapping(path = "/getStudent", method = RequestMethod.GET)
    @ApiOperation("/根据学生id获取学生信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    public List<Integer> getStudent(@RequestParam Integer id) {
        List<Integer> list = new ArrayList<>();
        int count = 5;
        while (count-- > 0) {
            list.add(id + count);
        }
        return list;
    }

    @RequestMapping(path = "/getStudent", method = RequestMethod.PATCH)
    @ApiOperation("/根据学生id获取学生信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "age", value = "年龄", required = true, paramType = "query", dataType = "int")
    })
    public Student editStudent(@RequestParam String name, @RequestParam Integer age) {
        Student student = new Student();
        student.setId(12);
        student.setName(name);
     //   student.setAge(age);
        return student;
    }

    @ApiOperation("/添加学生信息")
    @RequestMapping(path = "/addStudent", method = RequestMethod.POST)
    public Map<Integer, Student> AddStudent(@RequestBody Student student) {
        Map<Integer, Student> studentMap = new HashMap<>();
        studentMap.put(student.getId(), student);
        return studentMap;
    }
}