package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author: hujun
 * @date: 2021/03/05  15:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {

    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Long age;

    /**
     * 唯一标识id
     */
    private Integer id;


    @Override
    public boolean equals(Object anoObject) {
        if (this == anoObject) {
            return true;
        }
        if (anoObject instanceof Student) {
            Student anoStu = (Student) anoObject;
            if (anoStu.getName().equals(this.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 0;
        if (h == 0 && this.getName().length() > 0) {
            char val[] = this.getName().toCharArray();

            for (int i = 0; i < this.getName().length(); i++) {
                h = 31 * h + val[i];
            }
        }
        return h;
    }
}
