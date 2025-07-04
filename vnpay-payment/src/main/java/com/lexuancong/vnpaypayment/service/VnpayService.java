package com.lexuancong.vnpaypayment.service;

import com.lexuancong.vnpaypayment.viewmodel.VnpayCreatePaymentUrlRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VnpayService {
    public String createPaymentUrl(VnpayCreatePaymentUrlRequest vnpayCreatePaymentUrlRequest){
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "demo order";
        String orderType =  "other";
        String vnp_TxnRef =
        String vnp_IpAddr =
        String vnp_TmnCode =
        long amount = vnpayCreatePaymentUrlRequest.totalPrice()
                .multiply(BigDecimal.valueOf(100))
                .longValue();

    }
}
