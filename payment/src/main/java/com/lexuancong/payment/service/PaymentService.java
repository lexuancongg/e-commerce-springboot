package com.lexuancong.payment.service;

import com.lexuancong.payment.model.InitiatedPaymentVm;
import com.lexuancong.payment.repository.PaymentRepository;
import com.lexuancong.payment.service.handler.providers.ProviderPaymentHandler;
import com.lexuancong.payment.dto.InitPaymentRequest;
import com.lexuancong.payment.dto.InitPaymentResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final Map<String, ProviderPaymentHandler> providers = new HashMap<>();

    // container se innject all bean paymentProvider vao
    private final List<ProviderPaymentHandler> providerPaymentHandlers;


    // chaạy khi kởi tạo bean xong
    @PostConstruct
    public void initProviders (){
        for (ProviderPaymentHandler providerPaymentHandler : providerPaymentHandlers) {
            providers.put(providerPaymentHandler.getNameProvider() , providerPaymentHandler);
        }
    }

    public PaymentService(PaymentRepository paymentRepository, List<ProviderPaymentHandler> providerPaymentHandlers) {
        this.paymentRepository = paymentRepository;
        this.providerPaymentHandlers = providerPaymentHandlers;
    }

    public InitPaymentResponse initPayment(InitPaymentRequest initPaymentRequest){
      // xác định loại provider thanh toán
        ProviderPaymentHandler providerPaymentHandler = this.getProviderPaymentHandler(initPaymentRequest.paymentMethod());
        InitiatedPaymentVm initiatedPaymentVm = providerPaymentHandler.initPayment(initPaymentRequest);

        return new InitPaymentResponse(initiatedPaymentVm.status() , initiatedPaymentVm.paymentId() , initiatedPaymentVm.redirectUrl());
    }

    private ProviderPaymentHandler getProviderPaymentHandler(String providerName){
        ProviderPaymentHandler providerPaymentHandler = providers.get(providerName);
        if (providerPaymentHandler == null) {
            throw new RuntimeException("lỗi");
        }
        return providerPaymentHandler;
    }
}
