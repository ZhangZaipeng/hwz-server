package com.hwz.pay.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hwz.pay.service.PayService;
import com.hwz.pay.service.WxPayService;
import com.hwz.platform.YvanUtil;
import com.hwz.platform.platform.HttpParameterParser;
import com.hwz.platform.springmvc.JsonView;
import com.hwz.platform.springmvc.StringView;

@Controller
@RequestMapping("/api")
@Scope("prototype")
public class PayController {
	
	@Autowired
	private WxPayService  wxPayService;
	
	@Autowired
	private PayService payService;
	
	/**
	 * 统一调起支付接口 ，接受orderId，payType， userId调用service层goPay方法  
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping(value = "/go_pay.json", method = RequestMethod.POST)
    public ModelAndView goPay(HttpServletRequest request) throws JDOMException, IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		HttpParameterParser parser = HttpParameterParser.newInstance(request);
        
		//获取userId CustAgent custAgent = (CustAgent) identityValidator.currentPrincipal();
		
		// 解析   userId orderId payType
		
		// 用户userId
		String userId = "";
		
		// 支付订单号
        String orderId = parser.getString("orderId");
        
        // 支付方式 微信/支付宝
        String payType = parser.getString("payType");
        
        // 提交订单 （看业务需求）
        
        // 调用统一支付接口  获取前段调起  支付应用参数
        model = payService.goPay(request.getRemoteAddr(), orderId, payType, userId);
        
		return new ModelAndView(new JsonView(model));
	}
	
	/**
	 * 微信 回调处理
	 * @param request
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@RequestMapping(value = "/wx_pay_notify.json")
	public ModelAndView wxNotify(HttpServletRequest request) throws JDOMException, IOException {
		// 获取wx回调参数
		String strxml = YvanUtil.GetRequestBody(request);
		// 处理回调参数 
		String ret = wxPayService.wxPayNotify(strxml);
		
		return new ModelAndView(new StringView(ret));
	}
	
	/**
	 * 支付宝回调处理
	 * @param request
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@RequestMapping(value = "/alipay_notify.json")
	public ModelAndView aliNotify(HttpServletRequest request) throws JDOMException, IOException {
		
		//String strxml = YvanUtil.GetRequestBody(request);
		
		//String ret = wxPayService.wxPayNotify(strxml);
		String ret = "";
		return new ModelAndView(new StringView(ret));
	}
}
