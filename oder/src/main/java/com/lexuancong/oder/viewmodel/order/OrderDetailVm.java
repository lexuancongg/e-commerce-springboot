package com.lexuancong.oder.viewmodel.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.viewmodel.orderitem.OrderItemDetailVm;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

// get oder
public record OrderDetailVm(
        Long id,
        OrderStatus orderStatus,
        BigDecimal totalPrice,
        DeliveryStatus deliveryStatus,
        List<OrderItemDetailVm> orderItemVms,
        ZonedDateTime createAt

) {
    public static OrderDetailVm fromModel(Order order , List<OrderItem> orderItems) {
        List<OrderItemDetailVm> orderItemVms =  orderItems.stream()
                .map(OrderItemDetailVm::fromModel)
                .toList();

        return new OrderDetailVm(order.getId(),order.getOderStatus(),order.getTotalPrice(),
                order.getDeliveryStatus(),orderItemVms,order.getCreatedAt());

    }

}
