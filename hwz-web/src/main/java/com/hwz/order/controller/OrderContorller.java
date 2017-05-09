package com.hwz.order.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hwz.oil.service.OilOrderService;
import com.hwz.platform.Conv;
import com.hwz.platform.YvanUtil;
import com.hwz.platform.platform.HttpParameterParser;
import com.hwz.platform.springmvc.JsonView;

@Controller
@RequestMapping("/api/order")
public class OrderContorller {
	
	private OilOrderService oilOrderService;
	
	/**
	 * 通用生成订单
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderAdd.json", method = RequestMethod.POST)
    public ModelAndView orderAdd(HttpServletRequest request) {
		HttpParameterParser parser = HttpParameterParser.newInstance(request);
		 Map<String, Object> model = new HashMap<String, Object>();
		
		// 需要生成什么类型订单   比如oil
		String orderType = parser.getString("orderType");
		// json 数据
		String goodsInformation = parser.getString("goodsInformation");
		// 生成订单时 对应订单的商品数据
		switch (orderType) {
			case "oil":
				Map<String,Object> dataMap = YvanUtil.jsonToMap(goodsInformation);
				
				// 解析参数
				String oil_opt_id = Conv.NS(dataMap.get("oil_opt_id"));
				BigDecimal every_month_amuont = Conv.NDec(dataMap.get("every_month_amuont"));
				
				oilOrderService.prepOrder(oil_opt_id, every_month_amuont);
				break;
	
			case "gos":
				
				break;
			default:
				break;
		}
		// 返回订单号，返回
		
		return new ModelAndView(new JsonView(model));
	}
	
	/**
	 * 通用获取订单信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderAmount.json", method = RequestMethod.POST)
    public ModelAndView orderAmount(HttpServletRequest request) {
		ModelMap model = new ModelMap();
		
		
		return new ModelAndView(new JsonView(model));
	}
	

	
}
