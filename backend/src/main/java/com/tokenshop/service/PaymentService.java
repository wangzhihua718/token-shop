package com.tokenshop.service;

import com.tokenshop.common.Result;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付服务接口
 */
public interface PaymentService {

    /**
     * 创建支付宝支付订单
     */
    Map<String, String> createAlipayPayment(String orderNo, BigDecimal amount, String description);

    /**
     * 创建微信支付订单
     */
    Map<String, String> createWechatPayment(String orderNo, BigDecimal amount, String description);

    /**
     * 支付宝支付��调处理
     */
    Result<Void> handleAlipayNotify(Map<String, String> params);

    /**
     * 微信支付回调处理
     */
    Result<Void> handleWechatNotify(String body, String signature);
}
