package com.lexuancong.product.model.attribute;

import com.lexuancong.product.model.AuditEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_attribute_group")
@Getter
@Setter
// ví dụ thuộc tính thoong số kỹ thuật thì gồm nhiều thuộc tính khác như ram,...
public class ProductAttributeGroup extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
