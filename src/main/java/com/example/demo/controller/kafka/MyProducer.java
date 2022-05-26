package com.example.demo.controller.kafka;

/**
 * @author: hujun
 * @date: 2021/04/16  11:01
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * kafka依赖zookeeper，需要先启动zookeeper和kafka服务，并做一定的配置。
 * 代码主要是讲述操作执行流程，不能真正的跑起来。
 */
@RestController
public class MyProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping(value = "/kafka/normal")
    public void testNormal(@RequestParam String message) {
        //send（）可指定topic,partition,key
        kafkaTemplate.send("topic1", message);
    }

    @GetMapping(value = "/kafka/callback1")
    public void testCallback1(@RequestParam String message) {
        //send（）可指定topic,partition,key
        kafkaTemplate.send("topic1", message).addCallback(success -> {
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
        }, failure -> {
            System.out.println("发送消息失败:" + failure.getMessage());
        });
    }

    /**
     * 重点。mafka的生产者采用的是这种方式
     *
     * @param message
     */
    @GetMapping(value = "/kafka/callback2")
    public void testCallback2(@RequestParam String message) {
        kafkaTemplate.send("topic1", message).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送消息失败：" + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
            }
        });

    }
}

