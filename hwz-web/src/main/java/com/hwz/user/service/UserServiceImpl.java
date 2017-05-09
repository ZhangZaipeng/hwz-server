package com.hwz.user.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.NotSupportedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hwz.user.dao.StaffAgentMapper;
import com.hwz.user.dao.UserAgentMapper;
import com.hwz.user.dao.UserMapper;
import com.hwz.common.security.AuthenticationException;
import com.hwz.common.security.AuthenticationProvider;
import com.hwz.common.security.Principal;
import com.hwz.common.security.Verifier;
import com.hwz.platform.Conv;
import com.hwz.platform.StringUtils;
import com.hwz.platform.YvanUtil;
import com.hwz.platform.redis.RedisFactory;
import com.hwz.tools.AdminLoginVerifier;
import com.hwz.tools.AppLoginVerifier;
import com.hwz.tools.BCrypt;
import com.hwz.user.domain.StaffAgent;
import com.hwz.user.domain.User;
import com.hwz.user.domain.UserAgent;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class UserServiceImpl implements UserService, AuthenticationProvider{
	
	private static final String STAFF_CACHE = "STAFF_AGENT";
	private static final String USER_CACHE = "USER_AGENT";
	private static final String LOGIN_TOKEN = "TOKEN";
	
	@Autowired
	private RedisFactory redisFactory;
	
	@Autowired
	private UserAgentMapper userAgentMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private StaffAgentMapper staffAgentMapper;
	
	@Override
	public boolean regStaff(StaffAgent staffAgent) {
		// TODO Auto-generated method stub
		int a = staffAgentMapper.insertStaffAgent(staffAgent);
		if (a > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional(
			propagation=Propagation.REQUIRES_NEW,
			noRollbackFor={},
			isolation=Isolation.READ_COMMITTED,
			readOnly=false, 
			timeout=3
	)
	public boolean regUser(User user, UserAgent userAgent) {
		
		int result = userMapper.insert1(user);
		userAgent.setUserId(user.getUserId());
		int result1 = userAgentMapper.insertUserAgent(userAgent);
		if (result > 0 && result1 > 0) { // 都大于0  表示成功
			return true;
		} 
		return false;
	}
	
	@Override
	public Principal get(String uuid) {
		String cacheKey = LOGIN_TOKEN + ":" + uuid;
		Jedis jc = redisFactory.getResource();
		try {
			String jv = jc.get(cacheKey);
			if (StringUtils.isNullOrEmpty(jv)) {
				return null;
			}
			Map<String, Object> map = (Map<String, Object>) YvanUtil.jsonToMap(jv);
			String ids = map.get("id").toString();
			if (ids.startsWith("USER:")) {
				
				UserAgent userAgent = getUserAgentFromCache(Conv.NL(ids.substring(5)));
				if (userAgent != null) {
					return userAgent;
				}
				userAgent = userAgentMapper.selectByPrimaryKey(Conv.NL(ids.substring(5)));
				if (userAgent != null) {
					addToCache(userAgent);
					return userAgent;
				}
			} else if (ids.startsWith("STAFF:")) {
				StaffAgent staffAgent = getStaffAgentFromCache(Conv.NL(ids.substring(6)));
				if (staffAgent != null) {
					return staffAgent;
				}
				staffAgent = staffAgentMapper.selectByPrimaryKey(Conv.NL(ids.substring(6)));
				if (staffAgent != null) {
					addToCache(staffAgent);
					return staffAgent;
				}
			}
			return null;
		} finally {
			jc.close();
		}
	}

	@Override
	public Principal authenticate(Verifier verifier) throws AuthenticationException {
		
		if (verifier instanceof AppLoginVerifier) {
			// 移动端
			AppLoginVerifier appLoginVerifier = (AppLoginVerifier) verifier;
			
			UserAgent userAgent = userAgentMapper.selectByUserName(appLoginVerifier.getLoginName());
			
			if (userAgent == null) {
				throw new AuthenticationException("用户不存在");
			}
			
			if (userAgent.getDeleted() > 0) {
				throw new AuthenticationException("帐号已被冻结");
			}
			
			if (!BCrypt.checkpw(appLoginVerifier.getPassword(), userAgent.getLoginPwd())) {
				throw new AuthenticationException("密码不正确");
			}
			
			// 更新登录次数 最后登录时间
			userAgentMapper.loginSuccess(userAgent.getUserAgentId());
			
			addToCache(userAgent);
			
			return userAgent;
			
		} else if (verifier instanceof AdminLoginVerifier) { 
			// 后台登录
			AdminLoginVerifier adminLoginVerifier = (AdminLoginVerifier) verifier;
			
			StaffAgent staffAgent = staffAgentMapper.selectByUserName(adminLoginVerifier.getLoginName());
			
			if (staffAgent == null) {
				throw new AuthenticationException("用户不存在");
			}
			
			if (staffAgent.getStaffStatus() == null || staffAgent.getStaffStatus().intValue() != 1) {
				throw new AuthenticationException("帐号当前不允许登录");
			}
			
			if (!BCrypt.checkpw(adminLoginVerifier.getPassword(), staffAgent.getLoginPwd())) {
				throw new AuthenticationException("密码不正确");
			}
			
			staffAgentMapper.loginSuccess(staffAgent.getStaffAgentId());
			
			addToCache(staffAgent);
			
			return staffAgent;
		}
		
		throw new NotSupportedException("不支持的登录类型！");
	}

	@Override
	public Principal authenticate(Verifier verifier, String ip) throws AuthenticationException {
		// 暂不支持
		throw new NotSupportedException();
	}

	@Override
	public Date getLastRequestTime(String uuid) {
		String cacheKey = LOGIN_TOKEN + ":" + uuid;
		Jedis jc = redisFactory.getResource();
		try {
			String jv = jc.get(cacheKey);
			if (StringUtils.isNullOrEmpty(jv)) {
				return null;
			}
			Map<String, Object> map = (Map<String, Object>) YvanUtil.jsonToMap(jv);
			Long lastRequestTime = Conv.NL(map.get("lastRequestTime"));
			return new Date(lastRequestTime);
		} finally {
			jc.close();
		}
	}

	@Override
	public void setLastRequestTime(Date lastRequestTime, Serializable id, String uuid) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String cacheKey = LOGIN_TOKEN + ":" + uuid;
		
		Jedis jc = redisFactory.getResource();
		map.put("id", id);
		map.put("lastRequestTime", Conv.NS(lastRequestTime.getTime()));

		String ids = id.toString();
		int expireSec = 0;
		if (ids.startsWith("USER:")) {
			expireSec = Integer.MAX_VALUE;
		} else if (ids.startsWith("STAFF:")) {
			expireSec = Integer.MAX_VALUE;
		}
		try {
			String jsonValue = YvanUtil.toJson(map);
			jc.setex(cacheKey, expireSec, jsonValue);
		} finally {
			jc.close();
		}
		
	}

	@Override
	public Principal authenticate_auto(Verifier verifier) throws AuthenticationException {
		
		if (verifier instanceof AppLoginVerifier) {
			// 移动端登录
			AppLoginVerifier appLoginVerifier = (AppLoginVerifier) verifier;
			UserAgent userAgent = userAgentMapper.selectByUserName(appLoginVerifier.getLoginName());
			if (userAgent == null) {
				throw new AuthenticationException("用户不存在");
			}
			if (userAgent.getDeleted() > 0) {
				throw new AuthenticationException("帐号已被冻结");
			}

			userAgentMapper.loginSuccess(userAgent.getUserId());
			addToCache(userAgent);
			return userAgent;
		}

		throw new NotSupportedException("不支持的登录类型！");
		
	}
	
	private void addToCache(StaffAgent staffAgent) {
		String cacheKey = STAFF_CACHE + ":" + staffAgent.getStaffAgentId();
		Jedis jc = redisFactory.getResource();
		try {
			String jsonValue = YvanUtil.toJson(staffAgent);
			// 存放到redis中
			jc.setex(cacheKey, 120, jsonValue);
		} finally {
			jc.close();
		}
	}

	private void addToCache(UserAgent userAgent) {
		String cacheKey = USER_CACHE + ":" + userAgent.getUserAgentId();
		Jedis jc = redisFactory.getResource();
		try {
			String jsonValue = YvanUtil.toJson(userAgent);
			// 存放到redis中
			jc.setex(cacheKey, 120, jsonValue);
		} finally {
			jc.close();
		}
	}

	private UserAgent getUserAgentFromCache(Long custAgentId) {
		String cacheKey = USER_CACHE + ":" + custAgentId;
		Jedis jc = redisFactory.getResource();
		try {
			String jv = jc.get(cacheKey);
			
			if (!StringUtils.isNullOrEmpty(jv)) {
				return YvanUtil.jsonToObj(jv, UserAgent.class);
			}
			return null;
		} finally {
			jc.close();
		}
	}
	
	private StaffAgent getStaffAgentFromCache(Long staffAgentId) {
		String cacheKey = STAFF_CACHE + ":" + staffAgentId;
		Jedis jc = redisFactory.getResource();
		try {
			String jv = jc.get(cacheKey);
			if (!StringUtils.isNullOrEmpty(jv)) {
				return YvanUtil.jsonToObj(jv, StaffAgent.class);
			}
			return null;
		} finally {
			jc.close();
		}
	}

}
