package com.lexuancong.vnpaypayment.service;

import com.lexuancong.vnpaypayment.dto.VnpayCreatePaymentUrlRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VnpayService {
    public String createPaymentUrl(VnpayCreatePaymentUrlRequest vnpayCreatePaymentUrlRequest) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";

        String vnp_TmnCode = "mã website khi đki";
        // vnpay tính theo xu , 1vnd = 100 xu
        long amount = vnpayCreatePaymentUrlRequest.totalPrice()
                .multiply(BigDecimal.valueOf(100))
                .longValue();
        String vnp_Amount = String.valueOf(amount);
        String vnp_CreateDate = this.getCurrentVnpayDate();

        final  String vnp_CurrCode = "VND";
        String vnp_IpAddr  = this.getClientIp();
        String vnp_Locale  = "vn";
        String vnp_OrderInfo = vnpayCreatePaymentUrlRequest.description();
        String vnp_OrderType = "other";


        String vnp_ReturnUrl = "url frontend";
        String vnp_ExpireDate = this.getVnpayExpireDate(15);

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis()); // Mã giao dịch unique



        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount",  vnp_Amount);
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(fieldValue);
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = this.hmacSHA512("serect_key", hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" + query.toString();
        return paymentUrl;

    }

        private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating HMAC SHA512", e);
        }
    }

    public String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "unknown"; // không có request hiện tại
        }
        HttpServletRequest request = attrs.getRequest();

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }


    private  String getCurrentVnpayDate() {
        DateTimeFormatter VNPAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return now.format(VNPAY_FORMATTER);
    }

    private String getVnpayExpireDate(int minutesLater) {
        DateTimeFormatter VNPAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        ZonedDateTime expireTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
                .plusMinutes(minutesLater); // GMT+7
        return expireTime.format(VNPAY_FORMATTER);
    }


}
