package com.hwz.pay.util;

import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WxPaymentConfig {
	
    private String     appid;			// 应用ID
    private String     mchId;			// 商户号
    private String     notifyUrl;		// 回调地址
    private String     key;				// 支付密钥

    private boolean    wxIsPayTest;		// 是否测试
    private BigDecimal payTestOil;		// 油卡 测试金额
    private BigDecimal payTestGos;		// 自驾游  测试金额
    
    public WxPaymentConfig() {
    	
	}

	/**
     * 微信支付接口地址
     */
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    
    /**
     * 微信订单查询接口
     */
    public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    
    // * 服务号相关信息
    // */
//     public final static String APPID = ""; // 服务号的应用号
//     public final static String APP_SECRECT = "b71ae5b7a782bd77795f18f1910dc789"; // 服务号的应用密码
//     public final static String TOKEN = "weixinCourse"; // 服务号的配置token
    
    // // oauth2授权时回调action
    // public final static String REDIRECT_URI = "http://14.117.25.80:8016/GoMyTrip/a.jsp?port=8016";

    // // oauth2授权接口(GET)
    // public final static String OAUTH2_URL =
    // "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // // 刷新access_token接口（GET）
    // public final static String REFRESH_TOKEN_URL =
    // "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    // // 菜单创建接口（POST）
    // public final static String MENU_CREATE_URL =
    // "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    // // 菜单查询（GET）
    // public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    // // 菜单删除（GET）
    // public final static String MENU_DELETE_URL =
    // "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    // /**
    // * 微信支付接口地址
    // */
    // // 微信支付统一接口(POST)
    // // 微信退款接口(POST)
    // public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    // // 关闭订单接口(POST)
    // public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    // // 退款查询接口(POST)
    // public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    // // 对账单接口(POST)
    // public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    // // 短链接转换接口(POST)
    // public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    // // 接口调用上报接口(POST)
    // public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isWxIsPayTest() {
		return wxIsPayTest;
	}

	public void setWxIsPayTest(boolean wxIsPayTest) {
		this.wxIsPayTest = wxIsPayTest;
	}

	public BigDecimal getPayTestOil() {
		return payTestOil;
	}

	public void setPayTestOil(BigDecimal payTestOil) {
		this.payTestOil = payTestOil;
	}

	public BigDecimal getPayTestGos() {
		return payTestGos;
	}

	public void setPayTestGos(BigDecimal payTestGos) {
		this.payTestGos = payTestGos;
	}
   
}
