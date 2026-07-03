package com.tokenshop.controller;

import com.tokenshop.common.PageResult;
import com.tokenshop.common.Result;
import com.tokenshop.dto.order.CreateOrderDTO;
import com.tokenshop.entity.Order;
import com.tokenshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 订单控制器
 */
@Tag(name = "订单管理")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Operation(summary = "获取订单列表")
    @GetMapping
    public Result<PageResult<Order>> getOrderList(
            HttpServletRequest request,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "订单状态") @RequestParam(required = false) Integer status) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<Order> result = orderService.getOrderList(userId, page, size, status);
        return Result.success(result);
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        Order order = orderService.getById(id);
        return Result.success(order);
    }

    @Operation(summary = "创建订单")
    @PostMapping
    public Result<Order> createOrder(HttpServletRequest request, @Valid @RequestBody CreateOrderDTO requestDTO) {
        Long buyerId = (Long) request.getAttribute("userId");
        Order order = orderService.createOrder(buyerId, requestDTO.getTokenId(), requestDTO.getQuantity());
        return Result.success(order);
    }

    @Operation(summary = "取消订单")
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }

    @Operation(summary = "确认收货")
    @PutMapping("/{id}/confirm")
    public Result<Void> confirmOrder(@PathVariable Long id) {
        orderService.confirmOrder(id);
        return Result.success();
    }
}
