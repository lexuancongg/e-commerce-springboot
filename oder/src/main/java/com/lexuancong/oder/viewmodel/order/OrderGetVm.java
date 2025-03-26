package com.lexuancong.oder.viewmodel.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.viewmodel.orderitem.OrderItemGetVm;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

// get oder
public record OrderGetVm(
        Long id,
        OrderStatus orderStatus,
        BigDecimal totalPrice,
        DeliveryStatus deliveryStatus,
        List<OrderItemGetVm> orderItemVms,
        ZonedDateTime createAt

) {
    public static OrderGetVm fromModel(Order order , List<OrderItem> orderItems) {
        List<OrderItemGetVm> orderItemVms =  orderItems.stream()
                .map(OrderItemGetVm::fromModel)
                .toList();

        return new OrderGetVm(order.getId(),order.getOderStatus(),order.getTotalPrice(),
                order.getDeliveryStatus(),orderItemVms,order.getCreatedAt());

    }

}
