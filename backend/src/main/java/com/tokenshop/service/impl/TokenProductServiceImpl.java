package com.tokenshop.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tokenshop.common.PageResult;
import com.tokenshop.entity.TokenProduct;
import com.tokenshop.mapper.TokenProductMapper;
import com.tokenshop.service.TokenProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Token商品服务实现
 */
@Service
public class TokenProductServiceImpl extends ServiceImpl<TokenProductMapper, TokenProduct> implements TokenProductService {

    @Override
    public PageResult<TokenProduct> getTokenList(Integer page, Integer size, Long categoryId, String keyword) {
        LambdaQueryWrapper<TokenProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TokenProduct::getStatus, 1);
        if (categoryId != null) {
            wrapper.eq(TokenProduct::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(TokenProduct::getName, keyword);
        }
        wrapper.orderByDesc(TokenProduct::getCreatedAt);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<TokenProduct> p = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        this.page(p, wrapper);

        return new PageResult<>(p.getRecords(), p.getTotal(), (int) p.getCurrent(), (int) p.getSize());
    }

    @Override
    public TokenProduct createToken(TokenProduct token) {
        token.setStatus(2);
        this.save(token);
        return token;
    }

    @Override
    public void updateToken(TokenProduct token) {
        this.updateById(token);
    }

    @Override
    public void offlineToken(Long id) {
        TokenProduct token = this.getById(id);
        if (token != null) {
            token.setStatus(0);
            this.updateById(token);
        }
    }
}
