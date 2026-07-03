package com.tokenshop.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tokenshop.entity.Order;
import com.tokenshop.entity.OrderItem;
import com.tokenshop.entity.TokenProduct;
import com.tokenshop.mapper.OrderItemMapper;
import com.tokenshop.mapper.OrderMapper;
import com.tokenshop.mapper.TokenProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单定时任务
 */
@Slf4j
@Component
public class OrderTask {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private TokenProductMapper tokenProductMapper;

    /**
     * 每5分钟检查一次超时未支付订单
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional
    public void cancelExpiredOrders() {
        log.info("开始检查超时未支付订单...");

        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getStatus, 0)  // 待支付
               .lt(Order::getPayExpireTime, now);  // 已过期

        List<Order> expiredOrders = orderMapper.selectList(wrapper);

        for (Order order : expiredOrders) {
            try {
                // 恢复库存
                List<OrderItem> items = orderItemMapper.selectList(
                        new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
                for (OrderItem item : items) {
                    TokenProduct token = tokenProductMapper.selectById(item.getTokenId());
                    if (token != null) {
                        token.setStock(token.getStock() + item.getQuantity());
                        tokenProductMapper.updateById(token);
                    }
                }

                // 更新订单状态为已取消
                order.setStatus(4);
                orderMapper.updateById(order);

                log.info("订单 {} 已自动取消，原因：超时未支付", order.getOrderNo());
            } catch (Exception e) {
                log.error("取消超时订单失败，订单号: {}", order.getOrderNo(), e);
            }
        }

        log.info("超时订单检查完成，共处理 {} 个订单", expiredOrders.size());
    }
}
