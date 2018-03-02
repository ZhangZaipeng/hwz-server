package com.hwz.api.controller;

import javax.servlet.http.HttpServletRequest;

import com.hwz.api.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hwz.platform.springmvc.StringView;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;


    @RequestMapping(value = "/getId.json", method = RequestMethod.GET)
    public ModelAndView toLogin(HttpServletRequest request) {
    	
    	String sessionId = request.getSession().getId();
    	
        return new ModelAndView(new StringView(sessionId));
    }

    @RequestMapping(value = "/service.json", method = RequestMethod.GET)
    public ModelAndView testService(HttpServletRequest request) {

        String res = testService.testService("haha");

        return new ModelAndView(new StringView(res));
    }
}
