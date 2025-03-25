package com.lexuancong.oder.viewmodel;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;

public record OrderVm(
        Long id,
        String email,
        ShippingAddressVm shippingAddressVm,
        String note,
        int numberItem,
        BigDecimal totalPrice,
        OrderStatus oderStatus,
        DeliveryStatus deliveryStatus,
        Set<OrderItemVm> orderItemVms
) {
    public static OrderVm fromModel(Order order , Set<OrderItem> orderItemSet){
        OrderVm orderVm = new OrderVm();
    }

}
