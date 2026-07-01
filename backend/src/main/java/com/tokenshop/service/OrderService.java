package com.tokenshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tokenshop.common.PageResult;
import com.tokenshop.entity.Order;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 分页查询订单列表
     */
    PageResult<Order> getOrderList(Long userId, Integer page, Integer size, Integer status);

    /**
     * 创建订单
     */
    Order createOrder(Long buyerId, Long tokenId, Integer quantity);

    /**
     * 取消订单
     */
    void cancelOrder(Long orderId);

    /**
     * 确认收货
     */
    void confirmOrder(Long orderId);
}
