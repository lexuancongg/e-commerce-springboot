package com.lexuancong.oder.viewmodel.order;

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
        List<OrderItemGetVm> orderItemGetVms,
        ZonedDateTime createAt

) {

}
