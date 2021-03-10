package com.rigai.rigeye.common.kafka.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/16.
 */

public class ConsumerFactory {

    public static KafkaConsumer<String, String> createConsumer(Properties props){
        return new KafkaConsumer<>(props);
    }


    public static KafkaConsumer<String, String> simpleCreateConsumer(String server,String groupId,boolean isAutoCommit){
        //请勿擅自改配置，该配置有特殊用处
        Properties props=new Properties();
        props.put("bootstrap.servers", server);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", isAutoCommit);
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<>(props);
    }
}
