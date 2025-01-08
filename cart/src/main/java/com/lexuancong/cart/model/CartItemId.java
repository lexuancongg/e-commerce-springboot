package com.lexuancong.cart.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
// tự động tạo ra equas cà hashcode
@EqualsAndHashCode
public class CartItemId {
    private String customerId;
    private Long productId;

}
