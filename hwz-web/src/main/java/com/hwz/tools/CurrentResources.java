package com.hwz.tools;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hwz.common.security.IdentityValidator;
import com.hwz.common.security.Principal;
import com.hwz.user.domain.StaffAgent;
import com.hwz.user.service.StaffRoleResourceService;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class CurrentResources implements TemplateMethodModel{
	
    @Autowired
    private IdentityValidator   indentityValidator;
    
    @Autowired
    private StaffRoleResourceService staffRoleResourceService;
    
	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		Principal principal = indentityValidator.currentPrincipal();
        if (principal != null && principal instanceof StaffAgent) {
            StaffAgent staffAgent = (StaffAgent) indentityValidator.currentPrincipal();
            return staffRoleResourceService.getResourceListByRoleId(staffAgent.getRoleId());
        }
		return null;
	}

}
