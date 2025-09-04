package com.lexuancong.cart.repository;

import com.lexuancong.cart.model.CartItem;
import com.lexuancong.cart.model.CartItemId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    // khóa bi quan dùng để khóa dữ lieu nếu có nhiều thao tác cùng một row
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CartItem c WHERE c.customerId = :customerId AND c.productId = :productId")
    Optional<CartItem> findByCustomerIdAndProductId(String customerId, Long productId);

    // sort giam dan
    List<CartItem> findByCustomerIdOrderByCreatedAtDesc(String customerId);

    // delete
    void deleteByCustomerIdAndProductId(String customerId, Long productId);

    List<CartItem> findByCustomerIdAndProductIdIn(String customerId, Collection<Long> productIds);
}
