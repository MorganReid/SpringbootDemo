package com.example.demo.controller.thread;

/**
 * @author junhu
 * @date 2024/1/18
 */
public class TestThread {

    public static void main(String[] args) throws InterruptedException {
        TestThread test = new TestThread();


        TestThread test1 = new TestThread();
        Thread thread1 = new Thread(test::print);
        TestThread test2 = new TestThread();
        Thread thread2 = new Thread(test::print);
        thread1.start();
        thread2.start();
    }

    /**
     *    非静态方法的synchronized(等同普通方法的代码段加上synchronized(this))目的是为了防止多个线程同时执行同一个对象的同步代码段
     *
     *    当t1和t2使用的是test1和test2不同对象时,输出是随机的.修改如下
     *      第一种:t1和t2使用相同对象,synchronized加在方法上
     *      第二种:t1和t2使用不同对象,但普通方法的代码段加上synchronized(TestThread.class),也相当锁住同一个对象
     *
     *   但静态方法的synchronized是锁住整个类的Class对象,相当于全局锁
     */
    public synchronized void print() {

        for (int i = 0; i < 2; i++) {
            System.out.println(Thread.currentThread().getName() + ":  " + i);
        }

    }
}
