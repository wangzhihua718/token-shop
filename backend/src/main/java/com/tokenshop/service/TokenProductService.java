package com.tokenshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tokenshop.common.PageResult;
import com.tokenshop.entity.TokenProduct;

/**
 * Token商品服务接口
 */
public interface TokenProductService extends IService<TokenProduct> {

    /**
     * 分页查询Token列表
     */
    PageResult<TokenProduct> getTokenList(Integer page, Integer size, Long categoryId, String keyword);

    /**
     * 创建Token商品
     */
    TokenProduct createToken(TokenProduct token);

    /**
     * 更新Token商品
     */
    void updateToken(TokenProduct token);

    /**
     * 下架Token
     */
    void offlineToken(Long id);
}
