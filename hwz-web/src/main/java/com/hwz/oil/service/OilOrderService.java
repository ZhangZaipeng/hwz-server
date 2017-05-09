package com.hwz.oil.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hwz.pay.domain.PayCallBack;

public interface OilOrderService {
	 
	/**
	 * 油卡支付回调
	 */
	public boolean payFinish(PayCallBack payCallBack);
	
	/**
	 * 生产订单 
	 * 根据不同的 油卡类型oil_opt_id 和 every_month_amuont
	 * 生产订单 和 流水账 部分信息 
	 * @param oil_opt_id
	 * @param every_month_amuont
	 * @return
	 */
	public Map<String, Object> prepOrder(String oil_opt_id, BigDecimal every_month_amuont);
}
