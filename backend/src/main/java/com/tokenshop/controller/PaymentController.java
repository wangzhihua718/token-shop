package com.tokenshop.controller;

import com.tokenshop.common.Result;
import com.tokenshop.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付控制器
 */
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    public static class CreatePaymentRequest {
        private String orderNo;
        private BigDecimal amount;
        private String description;
        private String paymentMethod; // alipay, wechat

        public String getOrderNo() { return orderNo; }
        public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }

    @PostMapping("/create")
    public Result<Map<String, String>> createPayment(@RequestBody CreatePaymentRequest request) {
        Map<String, String> result;
        if ("alipay".equals(request.getPaymentMethod())) {
            result = paymentService.createAlipayPayment(request.getOrderNo(), request.getAmount(), request.getDescription());
        } else if ("wechat".equals(request.getPaymentMethod())) {
            result = paymentService.createWechatPayment(request.getOrderNo(), request.getAmount(), request.getDescription());
        } else {
            return Result.error("不支持的支付方式");
        }
        return Result.success(result);
    }

    @PostMapping("/alipay/notify")
    public Result<Void> handleAlipayNotify(@RequestBody Map<String, String> params) {
        return paymentService.handleAlipayNotify(params);
    }

    @PostMapping("/wechat/notify")
    public Result<Void> handleWechatNotify(@RequestBody String body, 
                                           @RequestHeader("Wechatpay-Signature") String signature) {
        return paymentService.handleWechatNotify(body, signature);
    }
}
