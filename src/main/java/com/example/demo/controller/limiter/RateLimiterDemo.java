package com.example.demo.controller.limiter;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.domain.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: hujun
 * @date: 2021/04/19  19:25
 */
public class RateLimiterDemo {

    public static void main(String[] args) throws JsonProcessingException {
        String format = String.format(
                "Fail: The %s for %s exists duplicated records，please check", 1, 2);
        System.out.println(format);
        Student student = new Student();
        System.out.println(student.getAge() > 0);
        String fileName = "aa.bb.xls";
        String substring = fileName.substring(0, fileName.lastIndexOf("."));
        System.out.println(substring);

        test();
        testListObjectToJson();
        RateLimiter rateLimiter = RateLimiter.create(2);
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            double acquire = rateLimiter.acquire();
            System.out.println(acquire + "----");
        }
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());

        System.out.println(start + "----" + end);
    }


    private static void testListObjectToJson() throws JsonProcessingException {
        List<Student> list = new ArrayList<>();
        list.add(new Student("name1,name111", 1L, 1));
        list.add(new Student("name2,name222", 1L, 1));
        list.add(new Student("name3,name333", 1L, 1));
        String string = JSONArray.toJSONString(list);
        System.out.println(string);
        List<Student> studentList = JSONArray.parseArray(string, Student.class);
        System.out.println(studentList.get(0).getName());

    }

    public static void testListToString() {
        basic basic1 = new basic(1, 0);
        basic basic2 = new basic(1, 0);
        basic basic3 = new basic(1, 2);
        List<basic> list = new ArrayList<>();
        list.add(basic1);
        list.add(basic2);
        list.add(basic3);
        Map<String, List<basic>> map = list.stream().collect(Collectors.groupingBy(s -> s.getId() + "-" + s.getType()));
        map.forEach((k, v) -> {
            System.out.println(v);
            int total = v.stream().collect(Collectors.toList()).size();
            System.out.println(total);


        });
        System.out.println(map.keySet().size());

    }

    public static void test() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("name1,name2,name1", 11L, 1));
        list.add(new Student("name1,name4", 11L, 2));
        list.add(new Student("name2,name3", 11L, 1));
        Set<String> set = Sets.newHashSet("name1", "name2", "name3");

        Map<Integer, List<Student>> map = list.stream().collect(Collectors.groupingBy(Student::getId));
        List<Student> res = new ArrayList<>();
        for (Integer key : map.keySet()) {
            List<Student> list1 = map.get(key);
            Set<String> sameKeyNameList = new HashSet<>();
            for (Student student : list1) {
                String[] split = student.getName().split(",");
                sameKeyNameList = buildSameKeyNameList(split, sameKeyNameList);
            }
            Set<String> notLegal = isLegal(set, sameKeyNameList);
            if (!CollectionUtils.isEmpty(notLegal)) {
                buildErrorMsg(notLegal);
            } else {
                String name = buildUserIdsByType(key, sameKeyNameList);
                res.add(new Student(name, 0L, key));
            }
        }

//        Set<Integer> sets = Sets.newHashSet(1, 2, 3, 4, 5, 6);
//        Set<Integer> sets2 = Sets.newHashSet(3, 4, 5, 6, 7, 8, 9);
//        // 交集
//        System.out.println("交集为：");
//        Sets.SetView<Integer> intersection = Sets.intersection(sets, sets2);
//        for (Integer temp : intersection) {
//            System.out.println(temp);
//        }
//        // 差集
//        System.out.println("差集为：");
//        Sets.SetView<Integer> diff = Sets.difference(sets, sets2);
//        for (Integer temp : diff) {
//            System.out.println(temp);
//        }
//        // 并集
//        System.out.println("并集为：");
//        Sets.SetView<Integer> union = Sets.union(sets, sets2);
//        for (Integer temp : union) {
//            System.out.println(temp);
//        }
    }

    private static void buildErrorMsg(Set<String> notLegal) {
        String join = StringUtils.join(notLegal.toArray(), ",");
        if (notLegal.size() == 1) {
            System.out.println("Fail: " + join + " is Invalid UserID, please have a check");
        } else {
            System.out.println("Fail: " + join + "are Invalid UserIDs, please have a check");
        }
    }

    private static String buildUserIdsByType(Integer key, Set<String> sameKeyNameList) {
        String join = StringUtils.join(sameKeyNameList.toArray(), ",");
        return join;
    }


    private static Set<String> isLegal(Set<String> set, Set<String> sameKeyNameList) {
        Sets.SetView<String> difference = Sets.difference(sameKeyNameList, set);
        if (!difference.isEmpty()) {
            return difference;
        } else {
            return null;
        }
    }

    private static Set<String> buildSameKeyNameList(String[] split, Set<String> sameKeyNameList) {
        for (String s : split) {
            sameKeyNameList.add(s);
        }
        return sameKeyNameList;
    }


    static class basic {
        private int id;
        private int type;

        public basic(int id, int type) {
            this.id = id;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public int getType() {
            return type;
        }
    }
}
