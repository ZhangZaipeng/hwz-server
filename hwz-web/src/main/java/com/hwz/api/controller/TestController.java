package com.hwz.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hwz.platform.springmvc.StringView;

@Controller
@RequestMapping("/test")
public class TestController {
	
	// 跳转至登录页面  git push
    @RequestMapping(value = "/getId.json", method = RequestMethod.GET)
    public ModelAndView toLogin(HttpServletRequest request) {
    	
    	String sessionId = request.getSession().getId();
    	
        return new ModelAndView(new StringView(sessionId));
    }
}
