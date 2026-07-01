package com.tokenshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tokenshop.common.PageResult;
import com.tokenshop.entity.Transaction;
import com.tokenshop.entity.Wallet;
import com.tokenshop.mapper.TransactionMapper;
import com.tokenshop.mapper.WalletMapper;
import com.tokenshop.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 钱包服务实现
 */
@Service
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements WalletService {

    @Resource
    private TransactionMapper transactionMapper;

    @Override
    public Wallet getWallet(Long userId) {
        LambdaQueryWrapper<Wallet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Wallet::getUserId, userId);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional
    public void recharge(Long userId, BigDecimal amount, String paymentMethod) {
        Wallet wallet = getWallet(userId);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setFrozenAmount(BigDecimal.ZERO);
            wallet.setStatus(1);
            this.save(wallet);
        }

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        this.updateById(wallet);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setType(1);
        transaction.setAmount(amount);
        transaction.setBalance(newBalance);
        transaction.setDescription("充值 - " + paymentMethod);
        transactionMapper.insert(transaction);
    }

    @Override
    @Transactional
    public void withdraw(Long userId, BigDecimal amount, String bankCard) {
        Wallet wallet = getWallet(userId);
        if (wallet == null) {
            throw new RuntimeException("钱包不存在");
        }
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);
        this.updateById(wallet);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setType(3);
        transaction.setAmount(amount);
        transaction.setBalance(newBalance);
        transaction.setDescription("提现到银行卡 " + bankCard);
        transactionMapper.insert(transaction);
    }

    @Override
    @Transactional
    public void deductBalance(Long userId, BigDecimal amount, String description) {
        Wallet wallet = getWallet(userId);
        if (wallet == null) {
            throw new RuntimeException("钱包不存在");
        }
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);
        this.updateById(wallet);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setType(2);
        transaction.setAmount(amount);
        transaction.setBalance(newBalance);
        transaction.setDescription(description);
        transactionMapper.insert(transaction);
    }

    @Override
    public PageResult<Transaction> getTransactionList(Long userId, Integer page, Integer size, Integer type) {
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getUserId, userId);
        if (type != null) {
            wrapper.eq(Transaction::getType, type);
        }
        wrapper.orderByDesc(Transaction::getCreatedAt);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Transaction> p = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        transactionMapper.selectPage(p, wrapper);

        return new PageResult<>(p.getRecords(), p.getTotal(), (int) p.getCurrent(), (int) p.getSize());
    }
}
