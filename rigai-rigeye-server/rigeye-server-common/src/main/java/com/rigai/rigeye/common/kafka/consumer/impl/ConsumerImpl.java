package com.rigai.rigeye.common.kafka.consumer.impl;

import com.rigai.rigeye.common.kafka.consumer.Consumer;
import com.rigai.rigeye.common.kafka.consumer.ConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/16.
 */

public class ConsumerImpl implements Consumer {

    private KafkaConsumer<String,String> kafkaConsumer;

    @Override
    public void init(Properties properties) {
        kafkaConsumer= ConsumerFactory.createConsumer(properties);
    }
;
    @Override
    public void init(KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaConsumer=kafkaConsumer;
    }

    @Override
    public ConsumerRecords<String, String> consumer(String topic ,long poll){
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer.poll(poll);
    }

    @Override
    public void destroy() {
        kafkaConsumer.close();
    }
}
