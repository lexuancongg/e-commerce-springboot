package com.lexuancong.oder.repository;

import com.lexuancong.oder.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOderId(Long oderId);
}
