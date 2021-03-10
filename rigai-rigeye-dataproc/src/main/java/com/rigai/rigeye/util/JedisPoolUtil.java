package com.rigai.rigeye.util;

import com.rigai.rigeye.common.ApolloConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.io.Serializable;

/**
 * @author yh
 * @date 2018/8/20 9:40
 */
public enum  JedisPoolUtil implements Serializable {
    INSTANCE;
    private JedisSentinelPool pool;

    JedisPoolUtil(){
        ApolloConfig config = ApolloConfig.INSTANCE;
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(config.getRedisMaxTotal());
        poolConfig.setMaxIdle(config.getRedisMaxIdle());
        pool = new JedisSentinelPool(config.getRedisMasterName(), config.getRedisSentinels(), poolConfig, config.getRedisAuth());
    }

    public Jedis getResource(){
        Jedis jedis = pool.getResource();
        jedis.select(ApolloConfig.INSTANCE.getRedisDb());
        return jedis;
    }

    public void close(Jedis jedis){
        if (jedis != null){
            jedis.close();
        }
    }
}
