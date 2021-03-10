package com.rigai.rigeye.common;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yh
 * @date 2018/9/14 9:35
 */
public enum ApolloConfig implements Serializable {
    /**
     * 单列
     */
    INSTANCE;
    private String influxUrl;
    private String influxUser ;
    private String influxPass ;
    private String influxDb ;

    /**Es配置**/
    private String esIp;
    private int esPort;

    /**redis*/
    private String redisAuth;
    private int redisDb;
    /** 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8 */
    private int redisMaxIdle;
    /** 最大连接数 */
    private int redisMaxTotal;
    /** 超时时间 */
    private int redisTimeOut;
    /** 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException； */
    private int redisMaxWaite;
    /** 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的； */
    private boolean redisTestOnBorrow;
    /**哨兵*/
    private Set<String> redisSentinels;
    private String redisMasterName;

    /**
     kafka配置
     自动重置offset到最大位置
     */
    private String kafkaAutoOffsetReset;
    /** 自动提交 */
    private boolean kafkaEnableAutoCommit;
    /** 每次最大拉取多少条数据 */
    private int kafkaMaxPollRecords;


    ApolloConfig(){
        Config config = ConfigService.getConfig("application");
        influxUrl = config.getProperty("influx.url","");
        influxUser = config.getProperty("influx.user","");
        influxPass = config.getProperty("influx.pass","");
        influxDb = config.getProperty("influx.db","");

        esIp = config.getProperty("es.ip","");
        esPort = config.getIntProperty("es.port",0);

        redisSentinels = new HashSet<>(Arrays.asList(config.getProperty("redis.host","").split(",")));
        redisAuth = config.getProperty("redis.password","");
        redisMasterName = config.getProperty("redis.masterName","");
        redisDb = config.getIntProperty("redis.db",0);
        redisMaxIdle = config.getIntProperty("redis.max.idle",8);
        redisMaxTotal = config.getIntProperty("redis.max.total",16);
        redisMaxWaite = config.getIntProperty("redis.max.waite",0);
        redisTimeOut = config.getIntProperty("redis.time.out",0);
        redisTestOnBorrow = config.getBooleanProperty("redis.test.on.borrow",true);

        kafkaAutoOffsetReset = config.getProperty("kafka.auto.offset.reset","latest");
        kafkaEnableAutoCommit = config.getBooleanProperty("kafka.enable.auto.commit",false);
        kafkaMaxPollRecords = config.getIntProperty("kafka.max.poll.records",2000);
    }

    public String getRedisMasterName() {
        return redisMasterName;
    }

    public String getInfluxUrl() {
        return influxUrl;
    }

    public String getInfluxUser() {
        return influxUser;
    }

    public String getInfluxPass() {
        return influxPass;
    }

    public String getInfluxDb() {
        return influxDb;
    }

    public String getEsIp() {
        return esIp;
    }

    public int getEsPort() {
        return esPort;
    }

    public Set<String> getRedisSentinels() {
        return redisSentinels;
    }

    public String getRedisAuth() {
        return redisAuth;
    }

    public int getRedisDb() {
        return redisDb;
    }

    public int getRedisMaxIdle() {
        return redisMaxIdle;
    }

    public int getRedisMaxTotal() {
        return redisMaxTotal;
    }

    public int getRedisTimeOut() {
        return redisTimeOut;
    }

    public int getRedisMaxWaite() {
        return redisMaxWaite;
    }

    public boolean isRedisTestOnBorrow() {
        return redisTestOnBorrow;
    }

    public String getKafkaAutoOffsetReset() {
        return kafkaAutoOffsetReset;
    }

    public boolean isKafkaEnableAutoCommit() {
        return kafkaEnableAutoCommit;
    }

    public int getKafkaMaxPollRecords() {
        return kafkaMaxPollRecords;
    }
}
