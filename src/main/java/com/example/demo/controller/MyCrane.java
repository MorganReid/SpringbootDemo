package com.example.demo.controller;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: hujun
 * @date: 2021/04/14  14:50
 */
public class MyCrane {
    public static void main(String[] args) throws InterruptedException {

        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        delayQueueTask();
        //scheduledExecutorServiceTask();


    }

    /**
     * quartz实现延迟任务
     *
     * @throws SchedulerException
     */
    public static void QuartzTask() throws SchedulerException {
        //调度器
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();

        //作业任务
        JobKey myJobKey = new JobKey("MyJobKey");
        JobDetail jobDetail = JobBuilder.newJob(MyCraneJob.class).withIdentity(myJobKey)
                .usingJobData("name", "hujun")
                .build();

        //触发器（simpleSchedule和cronSchedule）
        Trigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity("simpleTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(3))
                .build();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                .build();

        //触发器和任务组装到调度器里
        //scheduler.scheduleJob(jobDetail, simpleTrigger);
        scheduler.scheduleJob(jobDetail, cronTrigger);

        //  scheduler.deleteJob(myJobKey);
    }

    /**
     * ScheduledExecutorService 实现延迟任务
     */
    public static void scheduledExecutorServiceTask() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(
                new Runnable() {
                    @Override
                    public void run() {
                        // 执行任务的业务代码
                        System.out.println("执行任务" +
                                " ，执行时间：" + LocalDateTime.now());
                    }
                },
                // 初次执行间隔
                5,
                // 2s 执行一次
                2,
                TimeUnit.SECONDS);
    }

    /**
     * delayQueue实现延迟任务（重点。自研的Crane基于此）
     */
    public static void delayQueueTask() throws InterruptedException {
        DelayQueue delayQueue = new DelayQueue();
        // 添加延迟任务
        delayQueue.put(new DelayElement(1000));
        delayQueue.put(new DelayElement(3000));
        delayQueue.put(new DelayElement(5000));
        System.out.println("开始时间：" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        while (!delayQueue.isEmpty()) {
            // 执行延迟任务
            System.out.println(delayQueue.take());
        }
        System.out.println("结束时间：" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    private static class DelayElement implements Delayed {
        // 延迟截止时间（单面：毫秒）
        long delayTime = System.currentTimeMillis();

        public DelayElement(long delayTime) {
            this.delayTime = (this.delayTime + delayTime);
        }

        @Override
        // 获取剩余时间
        public long getDelay(TimeUnit unit) {
            return unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        // 队列里元素的排序依据
        public int compareTo(Delayed o) {
            if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
                return 1;
            } else if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return "延迟时间：" + new SimpleDateFormat("HH:mm:ss").format(new Date(delayTime));
        }
    }


    static class MyCraneJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            //获取jobDetail中传递的参数
            Object name = jobExecutionContext.getJobDetail().getJobDataMap().get("name");
            System.out.println(name + ":MyCraneJob---" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        }
    }
}
