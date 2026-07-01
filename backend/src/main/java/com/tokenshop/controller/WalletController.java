package com.tokenshop.controller;

import com.tokenshop.common.PageResult;
import com.tokenshop.common.Result;
import com.tokenshop.entity.Transaction;
import com.tokenshop.entity.Wallet;
import com.tokenshop.service.WalletService;


import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 钱包控制器
 */

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Resource
    private WalletService walletService;

    public static class RechargeRequest {
        private BigDecimal amount;
        private String paymentMethod;

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }

    public static class WithdrawRequest {
        private BigDecimal amount;
        private String bankCard;

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getBankCard() { return bankCard; }
        public void setBankCard(String bankCard) { this.bankCard = bankCard; }
    }

    @GetMapping
    
    public Result<Wallet> getWallet() {
        Long userId = 1L;
        Wallet wallet = walletService.getWallet(userId);
        return Result.success(wallet);
    }

    @PostMapping("/recharge")
    
    public Result<Void> recharge(@RequestBody RechargeRequest request) {
        Long userId = 1L;
        walletService.recharge(userId, request.getAmount(), request.getPaymentMethod());
        return Result.success();
    }

    @PostMapping("/withdraw")
    
    public Result<Void> withdraw(@RequestBody WithdrawRequest request) {
        Long userId = 1L;
        walletService.withdraw(userId, request.getAmount(), request.getBankCard());
        return Result.success();
    }

    @GetMapping("/transactions")
    
    public Result<PageResult<Transaction>> getTransactions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Integer type) {
        Long userId = 1L;
        PageResult<Transaction> result = walletService.getTransactionList(userId, page, size, type);
        return Result.success(result);
    }
}
