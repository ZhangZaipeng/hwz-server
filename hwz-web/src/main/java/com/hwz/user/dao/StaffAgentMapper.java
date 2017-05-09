package com.hwz.user.dao;

import com.hwz.platform.mybatis.DefaultMapper;
import com.hwz.user.domain.StaffAgent;

public interface StaffAgentMapper extends DefaultMapper{

	StaffAgent selectByUserName(String loginName) ;

	void loginSuccess(Long staffAgentId);
	
	int insertStaffAgent(StaffAgent staffAgent);

	StaffAgent selectByPrimaryKey(Long staffAgentId); 
}
