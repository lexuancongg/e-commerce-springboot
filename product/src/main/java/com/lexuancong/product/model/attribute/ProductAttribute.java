package com.lexuancong.product.model.attribute;

import com.lexuancong.product.model.AuditEntity;
import com.lexuancong.product.model.ProductAttributeValue;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_attribute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttribute extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_attribute_group_id")
    private ProductAttributeGroup productAttributeGroup;


    @OneToMany(mappedBy = "productAttribute")
    private List<ProductAttributeValue> productAttributeValues =new ArrayList<>();



}
