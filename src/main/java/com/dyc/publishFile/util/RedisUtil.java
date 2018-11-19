package com.dyc.publishFile.util;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedisUtil {
 //   private static final String HOST = "192.168.150.132";
//    private static final String HOST = "192.168.0.10";
//    private static final Integer PORT = 6379;



    public static Jedis getRedis(String HOST,Integer PORT){
        Jedis jedis = new Jedis(HOST,PORT);
        return jedis;
    }

    public static JedisCluster getRedisClute(String HOST,Integer PORT){
        HostAndPort hostAndPort = new HostAndPort(HOST, PORT);
        Set<HostAndPort> hostAndPortSet = new HashSet<>();
        hostAndPortSet.add(hostAndPort);
        return new JedisCluster(hostAndPortSet);
    }

    public static InputStream getInputStreamFromRedis(String redisFileUrl,JedisCluster  jedisCluster){
        byte[] bytes = jedisCluster.get(redisFileUrl.getBytes());
        return new ByteArrayInputStream(bytes);
    }

    public static Set<String> getAllKeys( JedisCluster  jedisCluster,String key){
        Set<String> redisKey = new HashSet<String>();
        Map<String, JedisPool> map = jedisCluster.getClusterNodes();
        for(String k : map.keySet()) {
            JedisPool jp = map.get(k);
            Jedis jedis = jp.getResource();
            Set<String> keys = jedis.keys(key);
            for(String str : keys){
                redisKey.add(str);
            }
        }
        return redisKey;
    }



}
