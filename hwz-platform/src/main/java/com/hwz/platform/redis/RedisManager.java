package com.hwz.platform.redis;

import java.io.Serializable;
import java.util.Date;

/**
 * 缓存管理接口
 * 类RedisManager.java的实现描述：TODO 类实现描述 
 * @author Administrator 2016年10月12日 上午9:31:58
 */
public interface RedisManager {

    /**
     * 得到缓存记录
     * 
     * @author jianguo.xu
     * @param key
     * @return
     */
    public Serializable get(String key);

    /**
     * 写入一条记录到缓存中
     * 
     * @author jianguo.xu
     * @param key 缓存key
     * @param result 缓存内容
     * @param timeToIdleSeconds 过期时间(单位秒)
     */
    public boolean put(String key, Serializable result, int timeToIdleSeconds);

    /**
     * 写入一条记录到缓存中
     * 
     * @author jianguo.xu
     * @param key 缓存key
     * @param result 缓存内容
     * @param idleDate 过期日期
     */
    public boolean put(String key, Serializable result, Date idleDate);

    /**
     * 删除一条缓存
     * 
     * @author jianguo.xu
     * @param key
     */
    public Object remove(String key);

    /**
     * 得到缓存数量
     * 
     * @author jianguo.xu
     * @return
     */
    public int getSize();

    /**
     * 清空缓存
     * 
     * @author jianguo.xu
     */
    public void clear();

    public long incr(String key);

    public long decr(String key);

}
