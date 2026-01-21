package com.lexuancong.oder.dto.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.ShippingAddress;
import com.lexuancong.oder.model.enum_status.DeliveryMethod;
import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.model.enum_status.PaymentMethod;
import com.lexuancong.oder.dto.orderitem.OrderItemGetResponse;
import com.lexuancong.oder.dto.shippingaddress.ShippingAddressGetResponse;

import java.math.BigDecimal;
import java.util.*;

public record OrderVm(
        Long id,
        String email,
        ShippingAddressGetResponse shippingAddressGetResponse,
        String note,
        int numberItem,
        BigDecimal totalPrice,
        OrderStatus oderStatus,
        DeliveryStatus deliveryStatus,
        List<OrderItemGetResponse> orderItemGetResponses,
        DeliveryMethod deliveryMethod,
        Long checkoutId,
        PaymentMethod paymentMethod
) {
    public static OrderVm fromModel(Order orderSaved, List<OrderItem> orderItemSet) {
        ShippingAddress shippingAddress = orderSaved.getShippingAddress();
        ShippingAddressGetResponse shippingAddressGetResponse = ShippingAddressGetResponse.fromShippingAddress(shippingAddress);

        // tránh lỗi NullPointerException
        List<OrderItemGetResponse> orderItemGetResponses = Optional.ofNullable(orderItemSet)
                // map xử lý optional
                .map(setOrderItem -> {
                    // map duyệt qua từng ptu
                    return orderItemSet.stream().map(OrderItemGetResponse::fromModel)
                            .toList();
                })
                .orElse(new ArrayList<>());


        return new OrderVm(
                orderSaved.getId(),
                orderSaved.getEmail(),
                shippingAddressGetResponse,
                orderSaved.getNote(),
                orderSaved.getNumberItem(),
                orderSaved.getTotalPrice(),
                orderSaved.getOderStatus(),
                orderSaved.getDeliveryStatus(),
                orderItemGetResponses,
                orderSaved.getDeliveryMethod(),
                orderSaved.getCheckoutId(),
                orderSaved.getPaymentMethod()
        );
    }

}
