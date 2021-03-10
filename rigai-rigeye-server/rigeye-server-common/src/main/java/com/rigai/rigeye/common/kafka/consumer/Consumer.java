package com.rigai.rigeye.common.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

/**
 * @author chenxing
 * Created by ChenXing on 2018/8/16.
 */

public interface Consumer {

    public void init(Properties properties);

    public void init(KafkaConsumer<String,String> kafkaConsumer);

    public ConsumerRecords<String, String> consumer(String topic , long poll);

    public void destroy();
}
