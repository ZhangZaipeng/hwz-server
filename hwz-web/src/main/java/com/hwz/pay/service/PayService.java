package com.hwz.pay.service;

import java.io.IOException;
import java.util.Map;

import org.jdom.JDOMException;

public interface PayService {
	/**
	 * 调起支付统一入口, 根据orderId，userId查库 ，组装支付参数调起微信支付宝支付  
	 * @param orderId
	 * @param payType
	 * @param userId
	 * @return
	 */
	public Map<String, Object> goPay(String ip, String orderId, String payType, String userId) throws JDOMException, IOException;
}
