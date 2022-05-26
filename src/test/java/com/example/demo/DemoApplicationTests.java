package com.example.demo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@SpringBootTest
class DemoApplicationTests {

    @Test
    public void testSelect() {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        try {

            new test().test1(countDownLatch);
            new test().test2(countDownLatch);
            countDownLatch.await();
            System.out.println("awaiy");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCrane() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("111--" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }, 1000, 1000);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " run " +
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }, 2, TimeUnit.SECONDS);
        executor.shutdown();
    }



}
