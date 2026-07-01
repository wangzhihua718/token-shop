package com.tokenshop.controller;

import com.tokenshop.common.PageResult;
import com.tokenshop.common.Result;
import com.tokenshop.entity.TokenProduct;
import com.tokenshop.service.TokenProductService;


import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Token商品控制器
 */

@RestController
@RequestMapping("/api/v1/tokens")
public class TokenProductController {

    @Resource
    private TokenProductService tokenProductService;

    @GetMapping
    
    public Result<PageResult<TokenProduct>> getTokenList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        PageResult<TokenProduct> result = tokenProductService.getTokenList(page, size, categoryId, keyword);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    
    public Result<TokenProduct> getTokenDetail(@PathVariable Long id) {
        TokenProduct token = tokenProductService.getById(id);
        return Result.success(token);
    }

    @PostMapping
    
    public Result<TokenProduct> createToken(@RequestBody TokenProduct token) {
        TokenProduct created = tokenProductService.createToken(token);
        return Result.success(created);
    }

    @PutMapping("/{id}")
    
    public Result<Void> updateToken(@PathVariable Long id, @RequestBody TokenProduct token) {
        token.setId(id);
        tokenProductService.updateToken(token);
        return Result.success();
    }

    @PutMapping("/{id}/offline")
    
    public Result<Void> offlineToken(@PathVariable Long id) {
        tokenProductService.offlineToken(id);
        return Result.success();
    }
}
