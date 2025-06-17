package com.lexuancong.product.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_option_value")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 1sp có nhiều option , 1 option c nhiều sp => bảng trung gian, 5 : 2 : "vàng"  , 5:2:"đỏ"
public class ProductOptionValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    private String value;

}
