package com.tokenshop.controller;

import com.tokenshop.common.PageResult;
import com.tokenshop.common.Result;
import com.tokenshop.entity.Transaction;
import com.tokenshop.entity.Wallet;
import com.tokenshop.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 钱包控制器
 */
@Tag(name = "钱包管理")
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

    @Operation(summary = "获取钱包信息")
    @GetMapping
    public Result<Wallet> getWallet(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Wallet wallet = walletService.getWallet(userId);
        return Result.success(wallet);
    }

    @Operation(summary = "充值")
    @PostMapping("/recharge")
    public Result<Void> recharge(HttpServletRequest request, @RequestBody RechargeRequest req) {
        Long userId = (Long) request.getAttribute("userId");
        walletService.recharge(userId, req.getAmount(), req.getPaymentMethod());
        return Result.success();
    }

    @Operation(summary = "提现")
    @PostMapping("/withdraw")
    public Result<Void> withdraw(HttpServletRequest request, @RequestBody WithdrawRequest req) {
        Long userId = (Long) request.getAttribute("userId");
        walletService.withdraw(userId, req.getAmount(), req.getBankCard());
        return Result.success();
    }

    @Operation(summary = "获取交易记录")
    @GetMapping("/transactions")
    public Result<PageResult<Transaction>> getTransactions(
            HttpServletRequest request,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "交易类型") @RequestParam(required = false) Integer type) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<Transaction> result = walletService.getTransactionList(userId, page, size, type);
        return Result.success(result);
    }
}
