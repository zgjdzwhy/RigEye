package com.rigai.rigeye.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.rigai.rigeye.common.BaseConstant;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.cluster.BrokerEndPoint;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.spark.streaming.kafka010.OffsetRange;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2018/7/9 14:10
 */
public enum  KafkaOffsetUtil {
    INSTANCE;

    /**
     * 获取topic 分区和offset
     * @param brokers
     * @param topics
     * @return
     */
    public Map<TopicPartition, Long> readOffsetsFromRedis(String brokers, Collection<String> topics, String group){
        //读取kafka最新partition offset
        Map<TopicPartition, Long> res = new HashMap<>(8);
        try (Jedis jedis = JedisPoolUtil.INSTANCE.getResource()) {
            Map<TopicPartition, Long> kafkaOffset = new HashMap<>(8);
            for (String topic : topics) {
                Map<TopicAndPartition, Long> offsets = getTopicOffsetsMap(brokers, topic);
                offsets.forEach((topicAndPartition, aLong) -> kafkaOffset.put(new TopicPartition(topicAndPartition.topic(), topicAndPartition.partition()), aLong));
            }

            String offsetsStr = jedis.get(BaseConstant.GROUP_PREFIX + group);
            if (offsetsStr == null) {
                //redis中无记录，直接使用最新的offset
                return kafkaOffset;
            } else {
                //读取redis中的offset
                Map<String, String> redisOffsets = (Map<String, String>) JSON.parse(offsetsStr);
                for (Map.Entry<String, String> entry : redisOffsets.entrySet()) {
                    Map<String, Integer> map = (Map) JSON.parse(entry.getKey());
                    for (Map.Entry<String, Integer> topicAndPartition : map.entrySet()) {
                        res.put(new TopicPartition(topicAndPartition.getKey(), topicAndPartition.getValue()), Long.valueOf(entry.getValue()));
                        break;
                    }
                }
                //对比更新offset
                kafkaOffset.forEach((topicPartition, aLong) -> {
                    if (!res.containsKey(topicPartition)) {
                        res.put(topicPartition, 0L);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("error "+e);
        }
        return res;
    }

    public void saveOffsetsToRedis(String group, OffsetRange[] offsetRanges){
        try (Jedis jedis = JedisPoolUtil.INSTANCE.getResource()) {
            Map<Map, String> offset = new HashMap<>(8);
            for (OffsetRange offsetRange : offsetRanges) {
                Map map = new HashMap(4);
                map.put(offsetRange.topic(), offsetRange.partition());
                offset.put(map, String.valueOf(offsetRange.untilOffset()));
            }
            jedis.set(BaseConstant.GROUP_PREFIX + group, JSON.toJSONString(offset));
        } catch (Exception e) {
            System.out.println("error "+e);
            throw new RuntimeException();
        }
    }

    public Map<TopicAndPartition, Long> getTopicOffsetsMap(String brokers, String topic) {
        Map<TopicAndPartition, Long> retVals = new HashMap<TopicAndPartition, Long>(8);
        for (String zkserver : brokers.split(",")) {
            SimpleConsumer simpleConsumer = new SimpleConsumer(zkserver.split(":")[0],
                    Integer.valueOf(zkserver.split(":")[1]), 100000, 1024,
                    "consumser");
            TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(Arrays.asList(topic));

            TopicMetadataResponse topicMetadataResponse = simpleConsumer.send(topicMetadataRequest);

            for (TopicMetadata metadata : topicMetadataResponse.topicsMetadata()) {
                for (PartitionMetadata part : metadata.partitionsMetadata()) {
                    BrokerEndPoint leader =  part.leader();
                    if (leader != null) {
                        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, part.partitionId());
                        PartitionOffsetRequestInfo partitionOffsetRequestInfo = new PartitionOffsetRequestInfo(
                                kafka.api.OffsetRequest.LatestTime(), 10000);
                        OffsetRequest offsetRequest = new OffsetRequest(
                                ImmutableMap.of(topicAndPartition, partitionOffsetRequestInfo),
                                kafka.api.OffsetRequest.CurrentVersion(), simpleConsumer.clientId());
                        OffsetResponse offsetResponse = simpleConsumer.getOffsetsBefore(offsetRequest);
                        if (!offsetResponse.hasError()) {
                            long[] offsets = offsetResponse.offsets(topic, part.partitionId());
                            retVals.put(topicAndPartition, offsets[0]);
                        }
                    }
                }
            }
            simpleConsumer.close();
        }
        return retVals;
    }
}
