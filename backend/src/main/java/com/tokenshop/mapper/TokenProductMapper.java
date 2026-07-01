package com.tokenshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tokenshop.entity.TokenProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * Token商品Mapper
 */
@Mapper
public interface TokenProductMapper extends BaseMapper<TokenProduct> {
}
