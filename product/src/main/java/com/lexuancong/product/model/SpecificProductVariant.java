package com.lexuancong.product.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "specific_product_variant")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// sự kết hợp giữa các tùy chọn => tạo ra biến thể sp , red + M , red + L , while + M ...
public class SpecificProductVariant {
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
