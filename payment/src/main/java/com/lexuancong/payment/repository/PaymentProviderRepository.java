package com.lexuancong.payment.repository;

import com.lexuancong.payment.model.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {
    List<PaymentProvider> findByEnabledIsTrue();
}
