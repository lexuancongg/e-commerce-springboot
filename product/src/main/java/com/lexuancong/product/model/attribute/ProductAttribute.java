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
// model lưu trữ thuộc tính của sản phẩm như màu sắc , kích thuowcs
public class ProductAttribute extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_attribute_group_id")
    private ProductAttributeGroup productAttributeGroup;


    // mục đích để sau này khi xóa attribute thì có thể get ra xem có chứa gtri chưa , nếu có thì k thể xóa
    @OneToMany(mappedBy = "productAttribute")
    private List<ProductAttributeValue> productAttributeValues =new ArrayList<>();



}
