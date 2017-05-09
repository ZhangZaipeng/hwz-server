package com.hwz.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hwz.user.dao.StaffRoleResourceMapper;
import com.hwz.platform.StringUtils;
import com.hwz.platform.YvanUtil;
import com.hwz.platform.redis.RedisFactory;

import redis.clients.jedis.Jedis;

@Service
public class StaffRoleResourceServiceImpl implements StaffRoleResourceService{
	
	private static final String ROLE_RESOURCE_CACHE = "ROLE_RESOURCE";
	
	@Autowired
	private RedisFactory redisFactory;
	
	@Autowired
    private StaffRoleResourceMapper  staffRoleResourceMapper;
	
	@Override
	public List<Map<String, Object>> getResourceListByRoleId(Integer roleId) {
		
		List<Map<String, Object>> ret = getFromCache(roleId);
		if (ret == null) {
            if (roleId == 99999) {
                ret = staffRoleResourceMapper.selectAllResource();
            } else {
                ret = staffRoleResourceMapper.selectResourceByRoleId(roleId);
            }

            addToCache(roleId, ret);
        }
		return ret;
	}
	
    @SuppressWarnings("unchecked")
	List<Map<String, Object>> getFromCache(Integer roleId) {
        String cacheKey = ROLE_RESOURCE_CACHE + ":" + roleId;

        Jedis jc = redisFactory.getResource();
        try {
            String jv = jc.get(cacheKey);
            if(jv != null){
                if (!StringUtils.isNullOrEmpty(jv.toString())) {
                    return YvanUtil.jsonToObj(jv.toString(), List.class);
                }
            }
            return null;
        } finally {
            jc.close();
        }
    }
    
    void addToCache(Integer roleId, List<Map<String, Object>> resources) {
        String cacheKey = ROLE_RESOURCE_CACHE + ":" + roleId;
        Jedis jc = redisFactory.getResource();
        
        try {
            String jsonValue = YvanUtil.toJson(resources);
            jc.setex(cacheKey, 120, jsonValue);
        } finally {
            jc.close();
        }
    }

    void removeCache(Integer roleId) {
        String cacheKey = ROLE_RESOURCE_CACHE + ":" + roleId;
        Jedis jc = redisFactory.getResource();
        try {
            jc.del(cacheKey);
        } finally {
            jc.close();
        }
    }

}
