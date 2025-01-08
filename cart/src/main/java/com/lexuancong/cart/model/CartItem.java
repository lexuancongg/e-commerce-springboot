package com.lexuancong.cart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cart_item")
// khóa chính phức hợp
@IdClass(CartItemId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
// lưu ý : trùng với tên và dữ liệu của trg class làm id phức hợp
public class CartItem extends AuditEntity{
    @Id
    private String customerId;
    @Id
    private Long productId;
    private int quantity;
}
