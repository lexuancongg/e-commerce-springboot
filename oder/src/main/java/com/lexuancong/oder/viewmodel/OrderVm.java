package com.lexuancong.oder.viewmodel;

import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OderStatus;

import java.math.BigDecimal;
import java.util.Set;

public record OrderVm(
        Long id,
        String email,
        ShippingAddressVm shippingAddressVm,
        String note,
        int numberItem,
        BigDecimal totalPrice,
        OderStatus oderStatus,
        DeliveryStatus deliveryStatus,
        Set<OrderItemVm> orderItemVms


) {

}
