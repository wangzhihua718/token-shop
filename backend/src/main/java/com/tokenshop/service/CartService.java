package com.tokenshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tokenshop.entity.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService extends IService<Cart> {

    /**
     * 添加商品到购物车
     */
    void addToCart(Long userId, Long tokenId, Integer quantity);

    /**
     * 获取购物车列表
     */
    List<Cart> getCartList(Long userId);

    /**
     * 更新购物车商品数量
     */
    void updateQuantity(Long cartId, Integer quantity);

    /**
     * 删除购物车商品
     */
    void removeFromCart(Long cartId);

    /**
     * 选中/取消选中商品
     */
    void toggleSelect(Long cartId, Integer selected);

    /**
     * 批量删除购物车商品
     */
    void batchRemove(Long userId, List<Long> cartIds);
}
