package com.hwz.tools;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hwz.common.security.IdentityValidator;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class CurrentUser implements TemplateMethodModel {

    @Autowired
    private IdentityValidator indentityValidator;

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        return indentityValidator.currentPrincipal();
    }

}
