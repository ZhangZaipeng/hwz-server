package com.hwz.pay.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 支付回调参数 实体
 */
public class PayCallBack {

    /**
     * 订单编号
     */
    private String     orderId;

    /**
     * 支付时间
     */
    private Timestamp  payTime;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付方式
     */
    private String     payOpt;

    /**
     * 支付是否成功
     */
    private boolean    paySuccess;

    /**
     * 接口消息
     */
    private String     msg;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayOpt() {
        return payOpt;
    }

    public void setPayOpt(String payOpt) {
        this.payOpt = payOpt;
    }

    public boolean isPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

