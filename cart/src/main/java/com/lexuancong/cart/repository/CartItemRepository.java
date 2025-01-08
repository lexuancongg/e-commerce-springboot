package com.lexuancong.cart.repository;

import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.model.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
