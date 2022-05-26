package com.example.demo.rateLimiter;

import com.example.demo.domain.locaTest;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author: hujun
 * @date: 2022/04/29  11:21
 */
public class RateLimiterTest {

    public static void main(String[] args) {


        locaTest locaTest = new locaTest();

        new Thread(new Runnable() {
            @Override
            public void run() {
                locaTest.setName("111");
                locaTest.setNameLocal("111local");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程1-----" + locaTest.getName());
                System.out.println("线程1-----" + locaTest.getNameLocal());


            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                locaTest.setName("222");
                locaTest.setNameLocal("222local");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程2-------" + locaTest.getName());
                System.out.println("线程2-----" + locaTest.getNameLocal());


            }
        }).start();


        RateLimiter rateLimiter = RateLimiter.create(5);
        while (true) {
            System.out.println("get 1 tokens: " + rateLimiter.acquire(5) + "s");

        }
    }
}
