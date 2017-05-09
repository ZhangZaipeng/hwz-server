package com.hwz.pay.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hwz.order.service.OrderService.ModuleType;
import com.hwz.pay.domain.PayCallBack;
import com.hwz.pay.util.CommonUtil;
import com.hwz.pay.util.PayCommonUtil;
import com.hwz.pay.util.WxPaymentConfig;
import com.hwz.pay.util.XMLUtil;
import com.hwz.platform.StringUtils;
import com.hwz.platform.YvanUtil;

@Service
public class WxPayServiceImpl implements WxPayService {
	
	Logger logger = Logger.getLogger(WxPayServiceImpl.class);
	
	@Autowired
    private WxPaymentConfig       wxPaymentConfig;
	
	@Override
	public Map<String, Object> wxPayment(String orderId, String body, BigDecimal fee, String Ip, String tradeType) throws JDOMException, IOException {
		
		TimeZone t = TimeZone.getTimeZone("Asia/Shanghai");
        Locale l = new Locale("zh", "CN");
        Calendar cl = Calendar.getInstance(t, l);
        long timeStamp = cl.getTimeInMillis() / 1000L;

        Map<String, Object> model = new HashMap<>();
        
        // 加上是否登陆逻辑
        
        // 判断订单号
        if (StringUtils.isNullOrEmpty(orderId) || orderId.length() < 3) {
            model.put("success", false);
            model.put("msg", "order_id error!");
            return model;
        }

        ModuleType orderType = ModuleType.getValue(orderId.substring(0, 3));

        // 是否是测试环境   测试环境更换金额
        if (wxPaymentConfig.isWxIsPayTest()) {
        	
            if (orderType == ModuleType.Gos) {
                fee = wxPaymentConfig.getPayTestGos();

            } else if (orderType == ModuleType.Oil) {
                fee = wxPaymentConfig.getPayTestOil();
            }
        }
        
        // 微信total_fee单位为分  free为元
        int total_fee = fee.multiply(new BigDecimal(100)).intValue();
        
        if (total_fee <= 0) {
            model.put("success", false);
            model.put("msg", "fee error!");
            return model;
        }
        
        orderId = orderId.substring(0, 18); // 前三个字符为订单类型
        model.put("orderId", orderId);		// 订单号
        model.put("body", body);			// 订单描述
        model.put("fee", fee);				// 订单金额
        model.put("timeStamp", timeStamp);	// 当前时间戳
        
        logger.info("wx-pay-client-request " + YvanUtil.toJson(model));

        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        
        parameters.put("appid", wxPaymentConfig.getAppid());
        parameters.put("mch_id", wxPaymentConfig.getMchId());
        parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
        parameters.put("body", body);
        parameters.put("out_trade_no", orderId);
        parameters.put("total_fee", total_fee);
        parameters.put("spbill_create_ip", Ip);
        parameters.put("notify_url", wxPaymentConfig.getNotifyUrl());
        parameters.put("trade_type", "APP");
        String sign = PayCommonUtil.createSign("UTF-8", parameters, wxPaymentConfig.getKey());
        parameters.put("sign", sign);
		
        // 把map中的参数转换为XML
        String requestXML = PayCommonUtil.getRequestXml(parameters);
        logger.info("wx-pay-request " + requestXML);
        
        // 请求微信服务接口
        String result = CommonUtil.httpsRequest(wxPaymentConfig.UNIFIED_ORDER_URL, "POST", requestXML);
        logger.info("wx-pay-response " + YvanUtil.toJson(result));
        
        // 解析微信返回的信息，以Map形式存储便于取值
        Map<String, Object> map = XMLUtil.doXMLParseMap(result);
        
        // 组装 App 调起支付需要的数据
        SortedMap<String, Object> clientParam = new TreeMap<String, Object>();
        clientParam.put("appid", wxPaymentConfig.getAppid());
        clientParam.put("partnerid", wxPaymentConfig.getMchId());
        clientParam.put("prepayid", map.get("prepay_id"));
        clientParam.put("package", "Sign=WXPay");
        clientParam.put("noncestr", map.get("nonce_str"));
        clientParam.put("timestamp", timeStamp);
        String clientSign = PayCommonUtil.createSign("UTF-8", clientParam, wxPaymentConfig.getKey());
        clientParam.put("sign", clientSign);

        clientParam.put("key", wxPaymentConfig.getKey());
        clientParam.put("result_code", map.get("result_code"));
        clientParam.put("return_code", map.get("return_code"));
        clientParam.put("out_trade_no", map.get("out_trade_no"));

        logger.info("wx-pay-client-response " + YvanUtil.toJson(map));
        
        return clientParam;
	}

	@Override
	public String wxPayNotify(String strxml) throws JDOMException, IOException {
		
		logger.info("wx-notify-request " + strxml);
		
		/* 微信回调 XML
			<xml>
			  <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
			  <attach><![CDATA[支付测试]]></attach>
			  <bank_type><![CDATA[CFT]]></bank_type>
			  <fee_type><![CDATA[CNY]]></fee_type>
			  <is_subscribe><![CDATA[Y]]></is_subscribe>
			  <mch_id><![CDATA[10000100]]></mch_id>
			  <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
			  <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
			  <out_trade_no><![CDATA[1409811653]]></out_trade_no>
			  <result_code><![CDATA[SUCCESS]]></result_code>
			  <return_code><![CDATA[SUCCESS]]></return_code>
			  <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
			  <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
			  <time_end><![CDATA[20140903131540]]></time_end>
			  <total_fee>1</total_fee>
			  <trade_type><![CDATA[JSAPI]]></trade_type>
			  <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
			</xml>
		*/
		
		
		// wxPayNotify 成功标识
		boolean orderSuccess = false;
		PayCallBack payCallback = new PayCallBack();
		
		Map<String, Object> ret = XMLUtil.doXMLParseMap(strxml) ;
		
		// out_trade_no 商户订单号 
		String orderId = ret.get("out_trade_no").toString();
		// 首先根据订单ID 查询订单是否是已完成 支付结束状态
		String orderFlag = "1"; 
		if ("1".equals(orderFlag)) { // 如果成功
			String result = "<xml><return_code>" + "SUCCESS" + "</return_code><return_msg>" + "OK"
			+ "</return_msg></xml>";
			return result;
		}
		
		// return_code 为 SUCCESS  result_code
		if ( "SUCCESS".equals(ret.get("return_code")) && "SUCCESS".equals(ret.get("result_code")) ) { // 
			// 验证 签名sign
			SortedMap<String, Object> notifyParams = new TreeMap<String, Object>();
			
			/*notifyParams.put("appid", wxPaymentConfig.getAppid());
			notifyParams.put("mch_id", wxPaymentConfig.getMchId());
			notifyParams.put("nonce_str", PayCommonUtil.CreateNoncestr());
			notifyParams.put("body", body);
			notifyParams.put("out_trade_no", orderId);
			notifyParams.put("total_fee", total_fee);
			notifyParams.put("spbill_create_ip", Ip);
			notifyParams.put("notify_url", wxPaymentConfig.getNotifyUrl());
	        notifyParams.put("trade_type", "APP");*/
			
			
			PayCommonUtil.createSign("UTF-8", notifyParams, wxPaymentConfig.getKey());
			// 根据订单号去流水表中获取sign
			String orderSign = "";
			if ( orderSign.equals(ret.get("return_code")) ) { //判断签名是否一样
				
			}
			
			orderSuccess = true;
		} else {
			String result = "<xml><return_code>" + "FAIL" + "</return_code>"
					+ "<return_msg>" + "验证失败" + "</return_msg></xml>";
			
		}
		
		
		// 
		String result = "";
		if ( orderSuccess ) { 
			result = "<xml><return_code>" + "SUCCESS" + "</return_code>" + "<return_msg>" + "订单支付成功" + "</return_msg></xml>";
			return result;
		} else {
			result = "<xml><return_code>" + "FAIL" + "</return_code>" + "<return_msg>" + "订单校验失败" + "</return_msg></xml>";
			return result;
		}
	}

}
