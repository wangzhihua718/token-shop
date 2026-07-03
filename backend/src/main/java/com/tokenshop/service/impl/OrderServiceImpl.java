package com.tokenshop.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tokenshop.common.BusinessException;
import com.tokenshop.common.PageResult;
import com.tokenshop.common.ResultCode;
import com.tokenshop.entity.Order;
import com.tokenshop.entity.OrderItem;
import com.tokenshop.entity.TokenProduct;
import com.tokenshop.mapper.OrderItemMapper;
import com.tokenshop.mapper.OrderMapper;
import com.tokenshop.mapper.TokenProductMapper;
import com.tokenshop.service.OrderService;
import com.tokenshop.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 订单服务实现
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private TokenProductMapper tokenProductMapper;

    @Resource
    private WalletService walletService;

    @Override
    public PageResult<Order> getOrderList(Long userId, Integer page, Integer size, Integer status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getBuyerId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreatedAt);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Order> p = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        this.page(p, wrapper);

        return new PageResult<>(p.getRecords(), p.getTotal(), (int) p.getCurrent(), (int) p.getSize());
    }

    @Override
    @Transactional
    public Order createOrder(Long buyerId, Long tokenId, Integer quantity) {
        // 检查 Token 是否存在
        TokenProduct token = tokenProductMapper.selectById(tokenId);
        if (token == null) {
            throw new BusinessException(ResultCode.TOKEN_NOT_FOUND);
        }

        // 检查 Token 是否下架
        if (token.getStatus() != 1) {
            throw new BusinessException(ResultCode.TOKEN_OFFLINE);
        }

        // 检查库存
        if (token.getStock() < quantity) {
            throw new BusinessException(ResultCode.STOCK_INSUFFICIENT);
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        order.setBuyerId(buyerId);
        order.setSellerId(token.getSellerId());
        order.setTotalAmount(token.getPrice().multiply(new BigDecimal(quantity)));
        order.setStatus(0);
        order.setPayExpireTime(LocalDateTime.now().plusMinutes(30));
        this.save(order);

        // 创建订单明细
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setTokenId(tokenId);
        item.setTokenName(token.getName());
        item.setQuantity(quantity);
        item.setUnitPrice(token.getPrice());
        item.setSubtotal(order.getTotalAmount());
        orderItemMapper.insert(item);

        // 扣减库存
        token.setStock(token.getStock() - quantity);
        tokenProductMapper.updateById(token);

        return order;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != 0) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "只能取消待支付订单");
        }
        
        // 恢复库存
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        for (OrderItem item : items) {
            TokenProduct token = tokenProductMapper.selectById(item.getTokenId());
            if (token != null) {
                token.setStock(token.getStock() + item.getQuantity());
                tokenProductMapper.updateById(token);
            }
        }

        order.setStatus(4);
        this.updateById(order);
    }

    @Override
    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != 2) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "只能确认已发货订单");
        }
        order.setStatus(3);
        this.updateById(order);
    }
}
