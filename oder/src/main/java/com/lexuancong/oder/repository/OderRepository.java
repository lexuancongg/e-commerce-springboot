package com.lexuancong.oder.repository;

import com.lexuancong.oder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OderRepository extends JpaRepository<Order, Long> {
}
