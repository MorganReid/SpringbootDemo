package com.example.demo.controller.kafka;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: hujun
 * @date: 2021/04/16  11:03
 */
@Component
public class MyConsumer {

    @Autowired
    private ConsumerFactory consumerFactory;

    // 消息过滤器
    @Bean
    public ConcurrentKafkaListenerContainerFactory myContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤策略
        factory.setRecordFilterStrategy(consumerRecord -> {
            if (Integer.parseInt(consumerRecord.value().toString()) % 2 == 0) {
                return false;
            }
            //返回true消息则被过滤
            return true;
        });
        return factory;
    }


    @Bean
    public ConsumerAwareListenerErrorHandler MyExceptionHandler() {
        return (message, exception, consumer) -> {
            System.out.println("消费异常：" + message.getPayload());
            return null;
        };
    }

    // 消费监听

    @KafkaListener(id = "consumer1", groupId = "felix-group", topicPartitions = {
            @TopicPartition(topic = "topic1", partitions = {"0"}),
            @TopicPartition(topic = "topic2", partitions = "0", partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))
    }, errorHandler = "MyExceptionHandler", containerFactory = "myContainerFactory")
    public void onMessage1(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("简单消费：" + record.topic() + "-" + record.partition() + "-" + record.value());
    }

    public static void main(String[] args) {
        int[]nums=new int[]{1,2,3};
        LinkedList<Integer> track=Lists.newLinkedList();
        System.out.println(res.size());
    }

    static List<LinkedList> res = Lists.newLinkedList();

    // 主函数
    boolean canPartitionKSubsets(int[] nums, int k) {
// 排除⼀些基本情况
        if (k > nums.length) return false;
        int sum = 0;
        for (int v : nums) sum += v;
        if (sum % k != 0) return false;
// k 个桶（集合），记录每个桶装的数字之和
        int[] bucket = new int[k];
// 理论上每个桶（集合）中数字的和
        int target = sum / k;
// 穷举，看看 nums 是否能划分成 k 个和为 target 的⼦集
        return backtrack(nums, 0, bucket, target);
    }
    // 递归穷举 nums 中的每个数字
    boolean backtrack(
            int[] nums, int index, int[] bucket, int target) {
        if (index == nums.length) {
// 检查所有桶的数字之和是否都是 target
            for (int i = 0; i < bucket.length; i++) {
                if (bucket[i] != target) {
                    return false;
                }
            }
// nums 成功平分成 k 个⼦集
            return true;
        }
// 穷举 nums[index] 可能装⼊的桶
        for (int i = 0; i < bucket.length; i++) {
// 剪枝，桶装装满了
            if (bucket[i] + nums[index] > target) {
                continue;
            }
// 将 nums[index] 装⼊ bucket[i]
            bucket[i] += nums[index];
// 递归穷举下⼀个数字的选择
            if (backtrack(nums, index + 1, bucket, target)) {
                return true;
            }
// 撤销选择
            bucket[i] -= nums[index];
        }
// nums[index] 装⼊哪个桶都不⾏
        return false; }
}
