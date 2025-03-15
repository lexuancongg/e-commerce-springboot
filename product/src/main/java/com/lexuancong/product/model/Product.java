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
    private long id;
    private String name;
    private String description;
    private String shortDescription;
    // thông số kỹ thuật
    private String specifications;
    // mã quản lý kho
    private String sku;
    // slug để hiển thị trên url
    private String slug;
    private String gtin;
    private Double price;
    private boolean hasOptions;
    private boolean isOrderEnable;
    private boolean isPublic;
    private boolean isFeature;
    // xác định cho các biến thể , chỉ có sp chính hiển thị đôc lập trên web
    private boolean isShownSeparately;
    // xacs định có theo dõi số lượng tồn koong
    private boolean isInventoryTracked;
    private Long inventoryQuantity;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private Long avatarImageId;

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
