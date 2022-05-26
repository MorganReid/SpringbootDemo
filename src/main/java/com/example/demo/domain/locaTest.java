package com.example.demo.domain;

/**
 * @author: hujun
 * @date: 2022/05/03  11:03
 */
public class locaTest {
    private ThreadLocal<String> nameLocal = new ThreadLocal<>();
    private String name;


    public String getNameLocal() {
        return this.nameLocal.get();
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal.set(nameLocal);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

