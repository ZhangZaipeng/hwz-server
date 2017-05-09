package com.hwz.pay.service;

import java.math.BigDecimal;
import java.util.Map;

public interface AliPayService {
	
	/**
	 * 生成APP支付订单信息
	 * @param orderId
	 * @param body
	 * @param fee
	 * @param Ip
	 * @param tradeType
	 * @return
	 */
	public Map<String, Object> aliPayment(String orderId, String body, BigDecimal fee, String Ip, String tradeType);
	
	/**
	 * aliPay支付会调处理
	 * @param strxml
	 * @return
	 */
	public String aliPayNotify (String strxml);
}
