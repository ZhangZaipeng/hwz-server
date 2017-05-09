package com.hwz.pay.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.jdom.JDOMException;

public interface WxPayService {
	/**
	 * 
	 * 微信统一下单支付接口，微信支付服务后台生成预支付交易单
	 *
	 * @param orderId 	订单号
	 * @param body 		订单商品描述
	 * @param fee 		订单金额
	 * @param Ip		用户IP 
	 * @param tradeType	交易类型 App Web ...
	 * 
	 * @return APP需要 调起支付 的参数。
	 */
	public Map<String, Object> wxPayment(String orderId, String body, BigDecimal fee, String Ip, String tradeType) throws JDOMException, IOException ;

	/**
	 * 微信回调处理
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public String wxPayNotify (String strxml) throws JDOMException, IOException ;
}
