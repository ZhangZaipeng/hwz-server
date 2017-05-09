package com.hwz.tools;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hwz.user.dao.StaffRoleResourceMapper;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class CurrentAllRole implements TemplateMethodModel{
	
	@Autowired
	private StaffRoleResourceMapper staffRoleResourceMapper;
	
	@Override
	public Object exec(@SuppressWarnings("rawtypes")List arguments) throws TemplateModelException {
		
		List<Map<String,Object>> roleMap = staffRoleResourceMapper.selectAllRole();
		
		return roleMap;
	}

}
