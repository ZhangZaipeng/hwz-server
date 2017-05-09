package com.hwz.user.dao;

import java.util.List;
import java.util.Map;

import com.hwz.platform.mybatis.DefaultMapper;

public interface StaffRoleResourceMapper extends DefaultMapper {

	List<Map<String, Object>> selectAllResource();

	List<Map<String, Object>> selectResourceByRoleId(Integer roleId);
	
	List<Map<String, Object>> selectAllRole();

}
