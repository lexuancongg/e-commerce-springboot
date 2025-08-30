package com.lexuancong.product.model.attribute;

import com.lexuancong.product.model.AuditEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "productAttributeGroup")
    private List<ProductAttribute> productAttributes = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof ProductAttributeGroup)) return false;
        return id != null && id.equals(((ProductAttributeGroup) o).id);
    }
}
