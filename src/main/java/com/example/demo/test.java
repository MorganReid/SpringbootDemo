package com.example.demo;

import java.util.concurrent.CountDownLatch;

/**
 * @author: hujun
 * @date: 2021/03/15  15:32
 */
public class test {
    public void test1(CountDownLatch countDownLatch) {
        countDownLatch.countDown();
        System.out.println("1111");
    }

    public void test2(CountDownLatch countDownLatch) {
        countDownLatch.countDown();
        System.out.println("222");
    }
}
