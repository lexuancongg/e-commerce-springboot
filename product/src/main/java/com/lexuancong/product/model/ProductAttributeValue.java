package com.lexuancong.product.model;

import com.lexuancong.product.model.attribute.ProductAttribute;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_attribute_value")
@Getter
@Setter
// một sp có nhiều thuộc tính và một thuộc tính cũng có nhieeuf sp => bảng trung gian
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id",nullable = false)
    private ProductAttribute productAttribute;

    private String value;

}
