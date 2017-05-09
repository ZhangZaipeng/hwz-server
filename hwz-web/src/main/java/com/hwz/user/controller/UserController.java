package com.hwz.user.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hwz.common.security.IdentityValidator;
import com.hwz.common.security.Principal;
import com.hwz.platform.platform.HttpParameterParser;
import com.hwz.platform.springmvc.JsonView;
import com.hwz.tools.AppLoginVerifier;
import com.hwz.tools.BCrypt;
import com.hwz.tools.ReturnMsg;
import com.hwz.user.domain.User;
import com.hwz.user.domain.UserAgent;
import com.hwz.user.service.UserService;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IdentityValidator identityValidator;
	
	/**
	 * 验证码 发送
	 */
	
	/**
     * 注册
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/register.json", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMsg register(HttpServletRequest request) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	
    	HttpParameterParser parser = HttpParameterParser.newInstance(request);
    	try {
        	
        	String loginUserName = parser.getString("loginUserName"); // 用户名
        	
        	String telephone = parser.getString("telephone"); // 电话号码
        	
        	String nick_name = parser.getString("nick_name"); // 真实姓名
        	
            String loginPassword = parser.getString("loginPassword"); // 密码
            
            String cryptString = BCrypt.hashpw(loginPassword, BCrypt.gensalt());
        	
            User user = new User();
            user.setMobile(telephone);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            user.setNickName(nick_name);
            
            UserAgent userAgent = new UserAgent();
            userAgent.setLoginName(loginUserName);
            userAgent.setLoginPwd(cryptString);
            userAgent.setTelephone(telephone);
            
            if (userService.regUser(user, userAgent)) {
            	
            	Principal principal = identityValidator.login(new AppLoginVerifier(loginUserName,loginPassword));
            	
            	return new ReturnMsg(ReturnMsg.SUCCESS,"注册成功",principal);
            }
		} catch (Exception e) {
			logger.info("用户注册异常 = "+e.getMessage(), e);
		}
    	return new  ReturnMsg(ReturnMsg.FAIL,"注册失败",model);
    }
	
	
	@RequestMapping(value = "/login.json", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		HttpParameterParser parser = HttpParameterParser.newInstance(request);
		String loginName = parser.getString("login_username");
		String pwd = parser.getString("login_password");
		
		Principal principal = identityValidator.login(new AppLoginVerifier(loginName, pwd));
		
		return new ModelAndView(new JsonView(principal));
	}
	
	@RequestMapping(value = "/getCurrentUser.json", method = RequestMethod.GET)
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
		
		HttpParameterParser parser = HttpParameterParser.newInstance(request);
		
		UserAgent custAgent = (UserAgent) identityValidator.currentPrincipal();
		
		
		return new ModelAndView(new JsonView(custAgent));
	}
	
}
