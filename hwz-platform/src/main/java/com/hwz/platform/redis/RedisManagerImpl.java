package com.hwz.platform.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hwz.platform.DateUtils;
import com.hwz.platform.DateUtils.TimeUnit;

public class RedisManagerImpl implements RedisManager {

    private static final StringRedisSerializer serializer = new StringRedisSerializer();

    private static final Logger                log        = LoggerFactory.getLogger(RedisManagerImpl.class);

    private boolean                            enable;

    
    private JedisConnectionFactory             jedisConnectionFactory;

    @PostConstruct
    public void initialize() throws Exception {
    }

    @PreDestroy
    public void shutDown() {
    }

    @Override
    public Serializable get(String key) {
        if (!enable) {
            return null;
        }
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        ByteArrayInputStream s = null;
        ObjectInputStream fo = null;
        try {
            byte[] bts = jedisConnection.get(serializer.serialize(key));
            if (bts == null) {
                return null;
            }
            s = new ByteArrayInputStream(bts);
            fo = new ObjectInputStream(s);
            return (Serializable) fo.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(s);
            IOUtils.closeQuietly(fo);
            jedisConnection.close();
        }
    }

    @Override
    public boolean put(String key, Serializable result, int timeToIdleSeconds) {
        if (!enable) {
            return false;
        }
        Date now = new Date();
        Date idleDate = DateUtils.add(now, TimeUnit.SECONDS, timeToIdleSeconds);

        return put(key, result, idleDate);
    }

    @Override
    public boolean put(String key, Serializable cacheResult, Date idleDate) {
        if (!enable) {
            return false;
        }
        if (cacheResult == null) {
            return false;
        }
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        ByteArrayOutputStream s = null;
        ObjectOutputStream fo = null;
        try {
            s = new ByteArrayOutputStream();
            fo = new ObjectOutputStream(s);
            fo.writeObject(cacheResult);
            long seconds = DateUtils.subtractSecond(idleDate, new Date());
            jedisConnection.setEx(serializer.serialize(key), seconds, s.toByteArray());
            return true;
        } catch (Exception e) {
            log.error("set memcached error", e);
            return false;
        } finally {
            IOUtils.closeQuietly(s);
            IOUtils.closeQuietly(fo);
            jedisConnection.close();
        }
    }

    @Override
    public Object remove(String key) {
        if (!enable) {
            return null;
        }
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        try {
            return jedisConnection.del(serializer.serialize(key));
        } finally {
            jedisConnection.close();
        }
    }

    @Override
    public int getSize() {
        if (!enable) {
            return 0;
        }
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        try {
            return Integer.getInteger(jedisConnection.dbSize().toString());
        } finally {
            jedisConnection.close();
        }
    }

    @Override
    public void clear() {
        if (!enable) {
            return;
        }
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        try {
            jedisConnection.flushAll();
        } finally {
            jedisConnection.close();
        }
    }

    @Override
    public long incr(String key) {
        if (!enable) {
            return 0;
        }
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        try {
            return jedisConnection.incr(serializer.serialize(key));
        } finally {
            jedisConnection.close();
        }
    }

    @Override
    public long decr(String key) {
        if (!enable) {
            return 0;
        }
        JedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        try {
            return jedisConnection.decr(serializer.serialize(key));
        } finally {
            jedisConnection.close();
        }
    }

    
    public boolean isEnable() {
        return enable;
    }

    
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
