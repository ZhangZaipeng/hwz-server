package com.hwz.order.service;

import java.math.BigDecimal;
import java.util.Map;

import javax.ws.rs.NotSupportedException;

public interface OrderService {
	
	/** 所用订单类型 ：  Oil 油卡订单  */
	public enum ModuleType { Oil ,Gos;
	
		public static ModuleType getValue(String value) {
			if ("OIL".equals(value)) {
				return ModuleType.Oil;
	
			} else if ("GOS".equals(value)) {
				return ModuleType.Gos;
	
			} else{
				throw new NotSupportedException("不支持的业务类型 " + value);
			}
			
		}
		
		public static String toString(ModuleType moduleType) {
			if (moduleType == ModuleType.Oil) {
				return "OIL";
	
			} else if (moduleType == ModuleType.Gos) {
				return "GOS";
	
			} else {
				throw new NotSupportedException("不支持的业务类型");
	
			}
		}
	}
	
	/**
	 * 获取订单编号
	 * 
	 * @param module
	 *            模块名称 OIL/GOS
	 * @param orderTime
	 *            订单时间  ( 时间戳 )
	 * @return 编号
	 */
	public String getOrderId(ModuleType moduleType, long orderTime);
	
	/**
	 * 根据油卡类型 计算订单金额
	 * @param oilOptId 油卡类型Id
	 * @param oilOrderAmuont 金额
	 * @return
	 */
	public Map<String,Object> oilPreOrder(Integer oilOptId,BigDecimal oilOrderAmuont);
	
	/**
	 * 根据自驾游类型 计算订单金额
	 * @param gosId
	 * @param oilOrderAmuont
	 * @return
	 */
	public Map<String,Object> gosPreOrder(Integer gosId,BigDecimal oilOrderAmuont);
}
