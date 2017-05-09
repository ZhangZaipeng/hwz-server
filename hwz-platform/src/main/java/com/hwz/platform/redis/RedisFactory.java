package com.hwz.platform.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by CPR210 on 2016/5/31.
 */
public interface RedisFactory {
    public Jedis getResource();
}
