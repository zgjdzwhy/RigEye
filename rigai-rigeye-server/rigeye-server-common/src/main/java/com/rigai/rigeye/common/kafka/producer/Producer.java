package com.rigai.rigeye.common.kafka.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * producer interface
 *
 * Asynchronously send will return immediately once the record has been stored in the buffer of
 * records waiting to be sent.
 *
 * kafka client above 0.9 provide no true synchronous interface to send message but there is an
 * annotation in the source code of kafka:
 * If you want to simulate a simple blocking call you can call the <code>get()</code> method immediately.
 *
 * so I use this method to make a simple synchronous interface
 *
 * @author chenxing
 */
public interface Producer {
    /**
     * asynchronous interface most fast one
     * @param topic topic
     * @param content 发送内容
     */
    public void asyncSend(String topic, String content);

    /**
     * complete Asynchronous interface , slower than asyncSend(String topic, byte[] content) but this method can
     * use call back to know if there is any exception
     * kafka config acks should not be 0
     * @param topic topic
     * @param content content to be send
     * @param callback a interface you can know if there is any exception happened
     * @return
     */
    public Future<RecordMetadata> asyncSend(String topic, String content, Callback callback);


    /**
     * simple Asynchronously send message to kafka
     * @param topic topic
     * @param content content to be send
     * @return
     */
    public RecordMetadata simpleSyncSend(String topic, String content) throws ExecutionException, InterruptedException;

    public void init(Properties properties);

    public void init(KafkaProducer<String,String> kafkaProducer);

    public void destroy(Long timeOut, TimeUnit timeUnit);
}
