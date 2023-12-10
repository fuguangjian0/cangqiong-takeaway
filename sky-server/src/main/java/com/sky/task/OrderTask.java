package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.sky.entity.Orders.*;

/**
 * 自定义定时任务, 实现订单状态定时处理
 * @author 付广建 2023/12/10 10:08
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理支付超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("处理支付超时订单: {}", new Date());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        // select * from orders where status=1 and order_time<当前时间-15min
        List<Orders> orderList = orderMapper.getByStatusAndOrdertimeLT(PENDING_PAYMENT, time);
        if (orderList != null && orderList.size()>0){
            orderList.forEach(orders -> {
                orders.setStatus(CANCELLED);
                orders.setCancelReason("支付超时, 自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            });
        }
    }

    /**
     * 处理派送中订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("处理派送中订单: {}", new Date());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        // select * from orders where status=4 and ordertime<当前时间-1小时
        List<Orders> ordersList = orderMapper.getByStatusAndOrdertimeLT(DELIVERY_IN_PROGRESS, time);
        if (ordersList!=null && ordersList.size()>0) {
            ordersList.forEach(orders -> {
                orders.setStatus(COMPLETED);
                orderMapper.update(orders);
            });
        }

    }

}
