package com.rigai.rigeye.common.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/10.
 */

public class ProducerFactory {

    public static KafkaProducer<String,String> createProducer(Properties props){
        return new KafkaProducer<>(props);
    }

    public static KafkaProducer<String,String> simpleCreateProducer(String server){
        Properties props=new Properties();
        props.put("bootstrap.servers",server);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaProducer<>(props);
    }
}
