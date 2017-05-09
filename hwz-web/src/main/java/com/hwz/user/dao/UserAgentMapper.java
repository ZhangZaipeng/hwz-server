package com.hwz.user.dao;

import com.hwz.platform.mybatis.DefaultMapper;
import com.hwz.user.domain.UserAgent;

public interface UserAgentMapper extends DefaultMapper{
	
	UserAgent selectByUserName(String loginName);

	int loginSuccess(Long userAgentId);
	
	int insertUserAgent(UserAgent userAgent);

	UserAgent selectByPrimaryKey(Long userAgentId); 

}
