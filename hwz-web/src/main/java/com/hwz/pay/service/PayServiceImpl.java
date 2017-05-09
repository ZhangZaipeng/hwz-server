package com.hwz.pay.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.NotSupportedException;

import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {
	
	@Autowired
	private WxPayService wxPayService;
	
	@Override
	public Map<String, Object> goPay(String ip, String orderId, String payType, String userId) throws JDOMException, IOException {
		Map<String, Object> model = new HashMap<>();
		
		// orderId --> body; orderId --> fee
		
		String body = "";			//商品描述
		BigDecimal fee = new BigDecimal(""); //商品金额
		
		if ("wx".equals(payType)) {
            model = wxPayService.wxPayment(orderId, body, fee, ip, "");
        } else if ("aliPay".equals(payType)) {

        } else {
            throw new NotSupportedException("不支持的支付类型 " + payType);
        }
		return model;
	}

}
