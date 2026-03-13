package com.lexuancong.oder.kafka.consumer;

import com.lexuancong.oder.constants.Constants;
import com.lexuancong.oder.kafka.message.PaymentResultMessage;
import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.repository.OrderRepository;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentResultConsumer {
    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_RESULT,
            containerFactory = "kafkaPaymentResultContainerFactory"
    )
    public void updateOrderPaymentStatus(PaymentResultMessage paymentResultMessage) {
        Long orderId = paymentResultMessage.orderId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.ORDER_NOT_FOUND));
        order.setPaymentStatus(paymentResultMessage.status());
        orderRepository.save(order);
    }

}
