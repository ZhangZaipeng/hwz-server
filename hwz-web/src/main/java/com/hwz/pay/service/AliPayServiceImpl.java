package com.hwz.pay.service;

import java.math.BigDecimal;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

public class AliPayServiceImpl implements AliPayService{

	@Override
	public Map<String, Object> aliPayment(String orderId, String body, BigDecimal fee, String Ip, String tradeType) {
		// TODO Auto-generated method stub
		
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "APP_ID", 
				"APP_PRIVATE_KEY", "json", "CHARSET", "ALIPAY_PUBLIC_KEY", "RSA2");
		
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(body);				// 商品描述
		model.setSubject("App支付测试Java");
		model.setOutTradeNo(orderId);		// 设置订单号
		model.setTimeoutExpress("30m");
		model.setTotalAmount(fee.toString());		// 商品金额
		model.setProductCode("QUICK_MSECURITY_PAY");
		
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl("商户外网可以访问的异步地址");
		
		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
		        System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
		} catch (AlipayApiException e) {
		        e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String aliPayNotify(String strxml) {
		// TODO Auto-generated method stub
		return null;
	}

}
