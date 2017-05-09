package com.hwz.user.service;

import com.hwz.user.domain.StaffAgent;
import com.hwz.user.domain.User;
import com.hwz.user.domain.UserAgent;

public interface UserService {
	
	/**
	 * 注册用户
	 * @param user
	 * @param userAgent
	 * @return
	 */
	boolean regUser(User user,UserAgent userAgent);
	
	/**
	 * 员工注册
	 * @param staffAgent
	 * @return
	 */
	boolean regStaff(StaffAgent staffAgent);
}
