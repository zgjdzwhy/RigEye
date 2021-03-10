package com.rigai.rigeye.common.kafka.producer.impl;

import com.rigai.rigeye.common.kafka.producer.Producer;
import com.rigai.rigeye.common.kafka.producer.ProducerFactory;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * @author ChenXing
 */
public class ProducerImpl implements Producer {

    private KafkaProducer<String,String> producer;

    @Override
    public void asyncSend(String topic, String content) {
        producer.send(new ProducerRecord<String,String>(topic,content));
    }

    @Override
    public Future<RecordMetadata> asyncSend(String topic, String content, Callback callback) {
        return producer.send(new ProducerRecord<String,String>(topic,content),callback);
    }

    @Override
    public RecordMetadata simpleSyncSend(String topic, String content) throws ExecutionException, InterruptedException {
        return producer.send(new ProducerRecord<String,String>(topic,content)).get();
    }

    @Override
    public void init(Properties properties) {
        this.producer= ProducerFactory.createProducer(properties);
    }

    @Override
    public void init(KafkaProducer<String,String> kafkaProducer) {
        this.producer=kafkaProducer;
    }

    @Override
    public void destroy(Long timeOut, TimeUnit timeUnit) {
        producer.close(timeOut, timeUnit);
    }

    public KafkaProducer<String,String> getProducer() {
        return producer;
    }

    public void setProducer(KafkaProducer<String,String> producer) {
        this.producer = producer;
    }


}
