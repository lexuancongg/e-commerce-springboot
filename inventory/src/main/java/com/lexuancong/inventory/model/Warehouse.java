package com.lexuancong.inventory.model;

import jakarta.persistence.*;
import lombok.*;

// entity kho chứa hàng
@Entity
@Table(name = "warehouse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class Warehouse extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 450)
    private String name;

    private Long addressId;

}
