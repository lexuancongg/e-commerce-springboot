package com.lexuancong.oder.repository;

import com.lexuancong.oder.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerId(String customerId, Sort sort);
}
