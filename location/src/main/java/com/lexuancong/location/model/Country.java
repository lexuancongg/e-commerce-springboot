package com.lexuancong.location.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class Country extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 450)
    private String name;



    @OneToMany(mappedBy = "country")
    private List<Province> provinces = new ArrayList<>();
}
