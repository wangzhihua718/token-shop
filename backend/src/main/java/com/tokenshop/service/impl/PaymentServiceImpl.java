package com.tokenshop.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tokenshop.common.Result;
import com.tokenshop.entity.Order;
import com.tokenshop.entity.Transaction;
import com.tokenshop.mapper.OrderMapper;
import com.tokenshop.mapper.TransactionMapper;
import com.tokenshop.service.PaymentService;
import com.tokenshop.service.WalletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付服务实现
 * 注意：这是模拟实现，实际接入需要配置真实的商户密钥
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private TransactionMapper transactionMapper;

    @Resource
    private WalletService walletService;

    @Override
    public Map<String, String> createAlipayPayment(String orderNo, BigDecimal amount, String description) {
        // 模拟支付宝支付，返回支付链接
        Map<String, String> result = new HashMap<>();
        result.put("paymentUrl", "https://openapi.alipay.com/gateway.do?out_trade_no=" + orderNo);
        result.put("orderNo", orderNo);
        result.put("amount", amount.toString());
        result.put("status", "pending");
        return result;
    }

    @Override
    public Map<String, String> createWechatPayment(String orderNo, BigDecimal amount, String description) {
        // 模拟微信支付，返回支付参数
        Map<String, String> result = new HashMap<>();
        result.put("appId", "wx_demo_app_id");
        result.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        result.put("nonceStr", IdUtil.fastSimpleUUID());
        result.put("package", "prepay_id=" + IdUtil.fastSimpleUUID());
        result.put("signType", "RSA");
        result.put("paySign", "demo_sign");
        result.put("orderNo", orderNo);
        result.put("amount", amount.toString());
        return result;
    }

    @Override
    public Result<Void> handleAlipayNotify(Map<String, String> params) {
        // 模拟支付宝回调处理
        String orderNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            // 更新订单状态
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Order::getOrderNo, orderNo);
            Order order = orderMapper.selectOne(wrapper);
            
            if (order != null && order.getStatus() == 0) {
                order.setStatus(1); // 已支付
                orderMapper.updateById(order);
                
                // 记录交易
                Transaction transaction = new Transaction();
                transaction.setUserId(order.getBuyerId());
                transaction.setType(1); // 充值
                transaction.setAmount(order.getTotalAmount());
                transaction.setBalance(order.getTotalAmount());
                transaction.setDescription("支付宝支付 - " + orderNo);
                transaction.setOrderNo(orderNo);
                transactionMapper.insert(transaction);
            }
        }
        
        return Result.success();
    }

    @Override
    public Result<Void> handleWechatNotify(String body, String signature) {
        // 模拟微信回调处理
        // 实际实现需要验证签名并解析XML/JSON
        return Result.success();
    }
}
