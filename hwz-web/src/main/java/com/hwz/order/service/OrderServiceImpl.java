package com.hwz.order.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hwz.platform.Constants;

@Service
public class OrderServiceImpl implements OrderService{
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyMMdd");
	
    @Autowired
    private IdCreatorService              idCreatorService;
	
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,
    	isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public String getOrderId(ModuleType moduleType, long orderTime) {
        String ds = SDF.format(new Date(orderTime));
        String idName = ModuleType.toString(moduleType) + ds;

        long idValue = idCreatorService.nextValue(idName);
        String str1 = Constants.ENV_ORDER + String.format("%08d", idValue);
        return idName + str1;
    }

	@Override
	public Map<String, Object> oilPreOrder(Integer oilOptId, BigDecimal oilOrderAmuont) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> gosPreOrder(Integer gosId, BigDecimal oilOrderAmuont) {
		// TODO Auto-generated method stub
		return null;
	}

}
