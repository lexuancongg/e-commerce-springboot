package com.lexuancong.oder.dto.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.ShippingAddress;
import com.lexuancong.oder.model.enum_status.DeliveryMethod;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.model.enum_status.PaymentStatus;
import com.lexuancong.oder.dto.shippingaddress.ShippingAddressResponse;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record OrderPreviewResponse(
        Long id,
        String email,
        ShippingAddressResponse shippingAddress,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        DeliveryMethod deliveryMethod,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt


) {
    public static OrderPreviewResponse fromOrder(Order order) {
        ShippingAddress shippingAddress = order.getShippingAddress();
        ShippingAddressResponse shippingAddressResponse = ShippingAddressResponse.fromShippingAddress(shippingAddress);
        return new OrderPreviewResponse(
                order.getId(),
                order.getEmail(),
                shippingAddressResponse,
                order.getTotalPrice(),
                order.getOderStatus(),
                order.getDeliveryMethod(),
                order.getPaymentStatus(),
                order.getCreatedAt()

        );
    }

}
