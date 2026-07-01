package com.tokenshop.controller;

import com.tokenshop.common.PageResult;
import com.tokenshop.common.Result;
import com.tokenshop.entity.Order;
import com.tokenshop.service.OrderService;


import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单控制器
 */

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    public static class CreateOrderRequest {
        private Long tokenId;
        private Integer quantity;

        public Long getTokenId() { return tokenId; }
        public void setTokenId(Long tokenId) { this.tokenId = tokenId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    @GetMapping
    
    public Result<PageResult<Order>> getOrderList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        Long userId = 1L;
        PageResult<Order> result = orderService.getOrderList(userId, page, size, status);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        Order order = orderService.getById(id);
        return Result.success(order);
    }

    @PostMapping
    
    public Result<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Long buyerId = 1L;
        Order order = orderService.createOrder(buyerId, request.getTokenId(), request.getQuantity());
        return Result.success(order);
    }

    @PutMapping("/{id}/cancel")
    
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }

    @PutMapping("/{id}/confirm")
    
    public Result<Void> confirmOrder(@PathVariable Long id) {
        orderService.confirmOrder(id);
        return Result.success();
    }
}
