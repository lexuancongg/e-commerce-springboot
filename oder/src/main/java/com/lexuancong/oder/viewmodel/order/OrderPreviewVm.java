package com.lexuancong.oder.viewmodel.order;

import com.lexuancong.oder.model.enum_status.DeliveryMethod;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.model.enum_status.PaymentStatus;
import com.lexuancong.oder.viewmodel.shippingaddress.ShippingAddressVm;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record OrderPreviewVm(
        Long id,
        String email,
        ShippingAddressVm shippingAddressVm,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        DeliveryMethod deliveryMethod,
        PaymentStatus paymentStatus,
        ZonedDateTime createAt


) {
}
