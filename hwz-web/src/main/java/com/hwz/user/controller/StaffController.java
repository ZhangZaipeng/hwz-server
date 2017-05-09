package com.hwz.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hwz.common.security.IdentityValidator;
import com.hwz.platform.StringUtils;
import com.hwz.platform.platform.HttpParameterParser;
import com.hwz.tools.AdminLoginVerifier;
import com.hwz.tools.BCrypt;
import com.hwz.tools.ReturnMsg;
import com.hwz.user.domain.StaffAgent;
import com.hwz.user.service.UserService;

@Controller
@RequestMapping("/staff")
public class StaffController {
	
	@Autowired
	private IdentityValidator identityValidator;
	
	@Autowired
	private UserService userService;
	
	// 跳转至登录页面
    @RequestMapping(value = "/login.htm", method = RequestMethod.GET)
    public ModelAndView toLogin(HttpServletRequest request) {
        return new ModelAndView("/admin/base/login.ftl");
    }
    
    // 跳转至注册页面
    @RequestMapping(value = "/toReg.htm", method = RequestMethod.GET)
    public ModelAndView toReg(HttpServletRequest request) {
        return new ModelAndView("/admin/base/reg.ftl");
    }
    
    // 跳转至主页
    @RequestMapping(value = "/main.htm", method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest request) {
        return new ModelAndView("/admin/base/main.ftl");
    }
    
    // 注册
    @RequestMapping(value = "/register.htm", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMsg reg(HttpServletRequest request) {
    	HttpParameterParser parser = HttpParameterParser.newInstance(request);
    	// 工号
    	String staffCode = parser.getString("staffCode");
    	// 姓名
    	String realName = parser.getString("realName");
    	// 电子邮箱
    	String email = parser.getString("email");
    	// 手机号
    	String telephone = parser.getString("telephone");
    	// 密码1
    	String pass1 = parser.getString("pass1");
    	// 密码2
    	String pass2 = parser.getString("pass2");
    	// 角色ID
    	Integer roleId = parser.getInteger("roleId");
    	
    	if (StringUtils.isNullOrEmpty(staffCode)) {
    		return new ReturnMsg(0, "工号不能为空!",null);
    	}
    	if (StringUtils.isNullOrEmpty(realName)) {
    		return new ReturnMsg(0, "姓名不能为空!",null);
    	}
    	if (StringUtils.isNullOrEmpty(telephone)) {
    		return new ReturnMsg(0, "手机号不能为空!",null);
    	}
    	
    	if (StringUtils.isNullOrEmpty(pass1)) {
    		return new ReturnMsg(0, "密码不能为空!",null);
    	}
    	if (StringUtils.isNullOrEmpty(pass2)) {
    		return new ReturnMsg(0, "重复密码不能为空!",null);
    	}
    	if(!pass1.equals(pass2)){
    		return new ReturnMsg(0, "密码不一致!",null);
    	}
    	if (null == roleId ) {
    		return new ReturnMsg(0, "角色ID不能为空!",null);
    	}
    	
    	String cryptString = BCrypt.hashpw(pass1, BCrypt.gensalt());
    	
    	StaffAgent staffAgent = new StaffAgent();
    	staffAgent.setStaffCode(staffCode);
    	staffAgent.setRealName(realName);
    	staffAgent.setEmail(email);
    	staffAgent.setTelephone(telephone);
    	staffAgent.setLoginPwd(cryptString);
    	staffAgent.setRoleId(roleId);
    	
    	if (userService.regStaff(staffAgent)) {
    		return new ReturnMsg(1, "注册成功！", null);
    	}
    	
        return new ReturnMsg(0, "注册失败！", null);
    }
    
    // 登录
    @RequestMapping(value = "/login.htm", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMsg loginPost(HttpServletRequest request) {
        HttpParameterParser parser = HttpParameterParser.newInstance(request);
        String loginName = parser.getString("loginName");
        String loginPassword = parser.getString("loginPwd");

        AdminLoginVerifier adminLoginVerifier = new AdminLoginVerifier(loginName, loginPassword);
        try {
            identityValidator.login(adminLoginVerifier);
        } catch (Exception e) {
            if (StringUtils.isNullOrEmpty(e.getMessage())) {
                return new ReturnMsg(ReturnMsg.FAIL,e.getClass().getName(),null);
            } else {
                return new ReturnMsg(ReturnMsg.FAIL,e.getMessage(),null);
            }
        }
        return new ReturnMsg(ReturnMsg.SUCCESS,"登陆成功",null);
    }

    @RequestMapping(value = "/logout.htm")
    public ModelAndView logout() {
        identityValidator.logout();
        return new ModelAndView(new RedirectView("/staff/login.htm"));
    }
}
