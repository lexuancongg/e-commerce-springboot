package com.lexuancong.oder.repository;

import com.lexuancong.oder.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> , JpaSpecificationExecutor<Order> {
    List<Order> findAllByCustomerId(String customerId, Sort sort);
}
