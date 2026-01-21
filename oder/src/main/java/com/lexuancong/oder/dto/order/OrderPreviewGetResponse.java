package com.lexuancong.oder.dto.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.ShippingAddress;
import com.lexuancong.oder.model.enum_status.DeliveryMethod;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.model.enum_status.PaymentStatus;
import com.lexuancong.oder.dto.shippingaddress.ShippingAddressGetResponse;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record OrderPreviewGetResponse(
        Long id,
        String email,
        ShippingAddressGetResponse shippingAddressGetResponse,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        DeliveryMethod deliveryMethod,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt


) {
    public static OrderPreviewGetResponse fromOrder(Order order) {
        ShippingAddress shippingAddress = order.getShippingAddress();
        ShippingAddressGetResponse shippingAddressGetResponse = ShippingAddressGetResponse.fromShippingAddress(shippingAddress);
        return new OrderPreviewGetResponse(
                order.getId(),
                order.getEmail(),
                shippingAddressGetResponse,
                order.getTotalPrice(),
                order.getOderStatus(),
                order.getDeliveryMethod(),
                order.getPaymentStatus(),
                order.getCreatedAt()

        );
    }

}
