package com.example.demo.controller.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @author: hujun
 * @date: 2021/04/16  11:32
 *
 *需要在application.propertise中配置自定义分区器，配置的值就是分区器类的全路径名，才会生效
 */

public class MyPartition implements Partitioner {
    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        /**
         * 自定义分区规则，假设全部达到0区
         */
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
