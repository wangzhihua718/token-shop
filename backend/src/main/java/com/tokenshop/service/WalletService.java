package com.tokenshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tokenshop.common.PageResult;
import com.tokenshop.entity.Transaction;
import com.tokenshop.entity.Wallet;

import java.math.BigDecimal;

/**
 * 钱包服���接口
 */
public interface WalletService extends IService<Wallet> {

    /**
     * 获取钱包信息
     */
    Wallet getWallet(Long userId);

    /**
     * 钱包充值
     */
    void recharge(Long userId, BigDecimal amount, String paymentMethod);

    /**
     * 钱包提现
     */
    void withdraw(Long userId, BigDecimal amount, String bankCard);

    /**
     * 扣减余额
     */
    void deductBalance(Long userId, BigDecimal amount, String description);

    /**
     * 分页查询交易记录
     */
    PageResult<Transaction> getTransactionList(Long userId, Integer page, Integer size, Integer type);
}
