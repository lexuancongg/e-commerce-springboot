package com.lexuancong.payment.service;

import com.lexuancong.payment.dto.*;
import com.lexuancong.payment.kafka.message.PaymentResultMessage;
import com.lexuancong.payment.model.Payment;
import com.lexuancong.payment.model.enumeration.PaymentMethod;
import com.lexuancong.payment.model.enumeration.PaymentStatus;
import com.lexuancong.payment.repository.PaymentRepository;
import com.lexuancong.payment.service.handler.providers.ProviderPaymentHandler;
import com.lexuancong.share.kafka.KafkaEventPublisher;
import com.lexuancong.share.kafka.KafkaTopics;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private final KafkaEventPublisher kafkaEventPublisher;
    private final Map<String, ProviderPaymentHandler> providers = new HashMap<>();

    private final List<ProviderPaymentHandler> providerPaymentHandlers;
    private final PaymentRepository paymentRepository;


    // chaạy khi kởi tạo bean xong
    @PostConstruct
    public void initProviders (){
        for (ProviderPaymentHandler providerPaymentHandler : providerPaymentHandlers) {
            providers.put(providerPaymentHandler.getNameProvider() , providerPaymentHandler);
        }
    }

    public PaymentService(KafkaEventPublisher kafkaEventPublisher, List<ProviderPaymentHandler> providerPaymentHandlers, PaymentRepository paymentRepository) {
        this.kafkaEventPublisher = kafkaEventPublisher;
        this.providerPaymentHandlers = providerPaymentHandlers;
        this.paymentRepository = paymentRepository;
    }

    public InitPaymentResponse initPayment(InitPaymentRequest initPaymentRequest){

        Payment payment = Payment.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .amount(initPaymentRequest.totalPrice())
                .paymentMethod(PaymentMethod.valueOf(initPaymentRequest.paymentMethod()))
                .orderId(initPaymentRequest.orderId())
                .build();
        paymentRepository.save(payment);

        ProviderPaymentHandler providerPaymentHandler = this.getProviderPaymentHandler(initPaymentRequest.paymentMethod());
        InitiatedPaymentResponse initiatedPaymentResponse =
                providerPaymentHandler.initPayment(initPaymentRequest);

        return new InitPaymentResponse(
                initiatedPaymentResponse.status() ,
                payment.getId(),
                String.format("%s?paymentId=%s?paymentMethod=%s?orderId=%s",
                        initiatedPaymentResponse.redirectUrl(),
                        payment.getId(),
                        initPaymentRequest.paymentMethod(),
                        initiatedPaymentResponse.orderId()
                )
        );
    }

    private ProviderPaymentHandler getProviderPaymentHandler(String providerName){
        ProviderPaymentHandler providerPaymentHandler = providers.get(providerName);
        if (providerPaymentHandler == null) {
            throw new RuntimeException("lỗi");
        }
        return providerPaymentHandler;
    }



    public CapturePaymentResponse capturePayment(CapturePaymentRequest capturePaymentRequest){
        ProviderPaymentHandler providerPaymentHandler = this.getProviderPaymentHandler(capturePaymentRequest.paymentMethod());
        CapturePaymentResponse capturePaymentResponse =
                providerPaymentHandler.capturePayment(capturePaymentRequest);
        Payment payment =  paymentRepository.findById(capturePaymentResponse.paymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setPaymentStatus(capturePaymentResponse.status());
        paymentRepository.save(payment);

        // kafka bắn tới order để update
        PaymentResultMessage paymentResultMessage = PaymentResultMessage.builder()
                .status(capturePaymentResponse.status())
                .paymentId(capturePaymentResponse.paymentId())
                .orderId(capturePaymentRequest.orderId())
                .build();
        kafkaEventPublisher.publish(KafkaTopics.PAYMENT_RESULT, paymentResultMessage);

        return capturePaymentResponse;
    }
}
