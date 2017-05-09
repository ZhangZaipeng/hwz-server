package com.hwz.user.dao;

import com.hwz.platform.mybatis.DefaultMapper;
import com.hwz.user.domain.User;

public interface UserMapper extends DefaultMapper{
	
	public User getUserById(Long userId);
	
	public int insert1(User user);
	
	public int insert2(User user);
	
}
