package com.tokenshop.dto.order;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 创建订单 DTO
 */
@Data
public class CreateOrderDTO {

    @NotNull(message = "Token ID 不能为空")
    private Long tokenId;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量至少为1")
    private Integer quantity;
}
