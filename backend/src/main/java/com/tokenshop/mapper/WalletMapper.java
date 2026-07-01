package com.tokenshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tokenshop.entity.Wallet;
import org.apache.ibatis.annotations.Mapper;

/**
 * 钱包Mapper
 */
@Mapper
public interface WalletMapper extends BaseMapper<Wallet> {
}
