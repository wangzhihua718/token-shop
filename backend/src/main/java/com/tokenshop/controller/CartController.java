package com.tokenshop.controller;

import com.tokenshop.common.Result;
import com.tokenshop.entity.Cart;
import com.tokenshop.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车控制器
 */
@Tag(name = "购物车管理")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/add")
    public Result<Void> addToCart(
            HttpServletRequest request,
            @Parameter(description = "Token ID") @RequestParam Long tokenId,
            @Parameter(description = "数量") @RequestParam(defaultValue = "1") Integer quantity) {
        Long userId = (Long) request.getAttribute("userId");
        cartService.addToCart(userId, tokenId, quantity);
        return Result.success();
    }

    @Operation(summary = "获取购物车列表")
    @GetMapping
    public Result<List<Cart>> getCartList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Cart> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/{id}/quantity")
    public Result<Void> updateQuantity(
            @PathVariable Long id,
            @Parameter(description = "数量") @RequestParam Integer quantity) {
        cartService.updateQuantity(id, quantity);
        return Result.success();
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/{id}")
    public Result<Void> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return Result.success();
    }

    @Operation(summary = "选中/取消选中商品")
    @PutMapping("/{id}/select")
    public Result<Void> toggleSelect(
            @PathVariable Long id,
            @Parameter(description = "是否选中: 0-未选中 1-选中") @RequestParam Integer selected) {
        cartService.toggleSelect(id, selected);
        return Result.success();
    }

    @Operation(summary = "批量删除购物车商品")
    @DeleteMapping("/batch")
    public Result<Void> batchRemove(
            HttpServletRequest request,
            @RequestBody Map<String, List<Long>> body) {
        Long userId = (Long) request.getAttribute("userId");
        List<Long> cartIds = body.get("cartIds");
        cartService.batchRemove(userId, cartIds);
        return Result.success();
    }
}
