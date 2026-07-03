package com.tokenshop.controller;

import com.tokenshop.common.PageResult;
import com.tokenshop.common.Result;
import com.tokenshop.entity.TokenProduct;
import com.tokenshop.service.TokenProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Token商品控制器
 */
@Tag(name = "Token 商品管理")
@RestController
@RequestMapping("/api/v1/tokens")
public class TokenProductController {

    @Resource
    private TokenProductService tokenProductService;

    @Operation(summary = "获取Token商品列表")
    @GetMapping
    public Result<PageResult<TokenProduct>> getTokenList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "12") Integer size,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        PageResult<TokenProduct> result = tokenProductService.getTokenList(page, size, categoryId, keyword);
        return Result.success(result);
    }

    @Operation(summary = "获取Token商品详情")
    @GetMapping("/{id}")
    public Result<TokenProduct> getTokenDetail(@PathVariable Long id) {
        TokenProduct token = tokenProductService.getById(id);
        return Result.success(token);
    }

    @Operation(summary = "创建Token商品")
    @PostMapping
    public Result<TokenProduct> createToken(@RequestBody TokenProduct token) {
        TokenProduct created = tokenProductService.createToken(token);
        return Result.success(created);
    }

    @Operation(summary = "更新Token商品")
    @PutMapping("/{id}")
    public Result<Void> updateToken(@PathVariable Long id, @RequestBody TokenProduct token) {
        token.setId(id);
        tokenProductService.updateToken(token);
        return Result.success();
    }

    @Operation(summary = "下架Token商品")
    @PutMapping("/{id}/offline")
    public Result<Void> offlineToken(@PathVariable Long id) {
        tokenProductService.offlineToken(id);
        return Result.success();
    }
}
