package com.hwz.platform.redis;

import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 用来生成 redis 客户端的工厂类
 * Created by zhangzp on 2016/4/29.
 */
public class RedisFactoryImpl implements RedisFactory, InitializingBean {
    private String address;
    private int maxTotal;
    private int maxIdle;
    private int maxWaitMillis;
    private boolean testOnBorrow;
    private boolean testOnRetrun;

    private JedisPool jedisPool;

    public Jedis getResource() {
        if(jedisPool == null){
            init();
        }
        return jedisPool.getResource();
    }


    private void init() {
        String[] spt2 = this.getAddress().split(":");

        JedisPoolConfig config = new JedisPoolConfig();

        //最大分配对象
        //config.setMaxTotal(1024);
        config.setMaxTotal(this.getMaxTotal());

        //最大能保持 idel状态的对象
        config.setMaxIdle(this.getMaxIdle());

        //当池内没有返回对象时，最大等待时间
        config.setMaxWaitMillis(this.getMaxWaitMillis());

        //当调用borrow Object方法时，是否进行有效性检查
        config.setTestOnBorrow(this.isTestOnBorrow());

        //当调用return Object方法时，是否进行有效性检查
        config.setTestOnReturn(this.isTestOnRetrun());

        jedisPool = new JedisPool(config, spt2[0], Integer.parseInt(spt2[1]));
    }

    @Override
    public String toString() {
        return this.address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnRetrun() {
        return testOnRetrun;
    }

    public void setTestOnRetrun(boolean testOnRetrun) {
        this.testOnRetrun = testOnRetrun;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
