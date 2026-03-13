package com.lexuancong.oder.dto.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.ShippingAddress;
import com.lexuancong.oder.model.enum_status.DeliveryMethod;
import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.model.enum_status.PaymentMethod;
import com.lexuancong.oder.dto.orderitem.OrderItemResponse;
import com.lexuancong.oder.dto.shippingaddress.ShippingAddressResponse;

import java.math.BigDecimal;
import java.util.*;

public record OrderResponse(
        Long id,
        String email,
        ShippingAddressResponse shippingAddress,
        String note,
        int numberItem,
        BigDecimal totalPrice,
        OrderStatus oderStatus,
        DeliveryStatus deliveryStatus,
        List<OrderItemResponse> orderItems,
        DeliveryMethod deliveryMethod,
        Long checkoutId,
        PaymentMethod paymentMethod
) {
    public static OrderResponse from(Order orderSaved, List<OrderItem> items) {
        ShippingAddress shippingAddress = orderSaved.getShippingAddress();
        ShippingAddressResponse shippingAddressResponse =
                ShippingAddressResponse.fromShippingAddress(shippingAddress);

        List<OrderItemResponse> orderItems = Optional.ofNullable(items)
                .map(item -> {
                    return items.stream().map(OrderItemResponse::fromOrderItem)
                            .toList();
                })
                .orElse(new ArrayList<>());


        return new OrderResponse(
                orderSaved.getId(),
                orderSaved.getEmail(),
                shippingAddressResponse,
                orderSaved.getNote(),
                orderSaved.getNumberItem(),
                orderSaved.getTotalPrice(),
                orderSaved.getOderStatus(),
                orderSaved.getDeliveryStatus(),
                orderItems,
                orderSaved.getDeliveryMethod(),
                orderSaved.getCheckoutId(),
                orderSaved.getPaymentMethod()
        );
    }

}
