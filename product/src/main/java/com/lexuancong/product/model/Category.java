package com.lexuancong.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "category")
public class Category extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String slug;
    private boolean isPublic;
    // avatar category
    private Long imageId;


    @ManyToOne()
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.REMOVE)
    // yêu cầu jackson bỏ qua khi convert sang json và ngược lại cho trường này
    @JsonIgnore
    private List<Category> child = new ArrayList<>();


    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<ProductCategory> productCategories = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Category)) return false;
        return  id!=null && id.equals(((Category)o).id);
    }







}
