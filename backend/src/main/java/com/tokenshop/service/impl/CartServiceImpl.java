package com.tokenshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tokenshop.common.BusinessException;
import com.tokenshop.common.ResultCode;
import com.tokenshop.entity.Cart;
import com.tokenshop.entity.TokenProduct;
import com.tokenshop.mapper.CartMapper;
import com.tokenshop.mapper.TokenProductMapper;
import com.tokenshop.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购物车服务实现
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Resource
    private TokenProductMapper tokenProductMapper;

    @Override
    @Transactional
    public void addToCart(Long userId, Long tokenId, Integer quantity) {
        // 检查 Token 是否存在
        TokenProduct token = tokenProductMapper.selectById(tokenId);
        if (token == null) {
            throw new BusinessException(ResultCode.TOKEN_NOT_FOUND);
        }

        // 检查是否已在购物车中
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getTokenId, tokenId);
        Cart existingCart = this.getOne(wrapper);

        if (existingCart != null) {
            // 更新数量
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            this.updateById(existingCart);
        } else {
            // 新增
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setTokenId(tokenId);
            cart.setTokenName(token.getName());
            cart.setTokenImage(token.getImage());
            cart.setPrice(token.getPrice());
            cart.setQuantity(quantity);
            cart.setSelected(1);
            this.save(cart);
        }
    }

    @Override
    public List<Cart> getCartList(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .orderByDesc(Cart::getUpdatedAt);
        return this.list(wrapper);
    }

    @Override
    public void updateQuantity(Long cartId, Integer quantity) {
        if (quantity < 1) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "数量至少为1");
        }

        Cart cart = this.getById(cartId);
        if (cart == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }

        cart.setQuantity(quantity);
        this.updateById(cart);
    }

    @Override
    public void removeFromCart(Long cartId) {
        this.removeById(cartId);
    }

    @Override
    public void toggleSelect(Long cartId, Integer selected) {
        Cart cart = this.getById(cartId);
        if (cart == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }

        cart.setSelected(selected);
        this.updateById(cart);
    }

    @Override
    @Transactional
    public void batchRemove(Long userId, List<Long> cartIds) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .in(Cart::getId, cartIds);
        this.remove(wrapper);
    }
}
