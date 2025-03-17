package com.lexuancong.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String shortDescription;
    // thông số kỹ thuật
    private String specifications;
    // mã quản lý kho
    private String sku;
    // slug để hiển thị trên url
    private String slug;
    // mã vạch quốc tế
    private String gtin;
    private Double price;
    // cs biến thể hay không
    private boolean hasOptions;
    // cho phép oder hay không
    private boolean isOrderEnable;
    // hiển thị hay không
    private boolean isPublic;

    private boolean isFeature;
    // xác định cho các biến thể , chỉ có sp chính hiển thị đôc lập trên web
    private boolean isShownSeparately;
    // xacs định có theo dõi số lượng tồn không để kiểm tra trong quá trình đặt hàng
    private boolean isInventoryTracked;
    // số luownjng tồn kho
    private Long inventoryQuantity;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    // hình id hình ảnh đại diện
    private Long avatarImageId;


    // thuoojc tính để xác định phí vận chuyển cho sau này
    private Double length;
    private Double width;
    private Double height;
    private Double weight;

    // các thuộc tính khác

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST} )
    @Builder.Default
    private List<ProductCategory> productCategories = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<ProductAttributeValue> attributeValues = new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Product parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @JsonIgnore
    @Builder.Default
    private List<Product> child = new ArrayList<>();






}
