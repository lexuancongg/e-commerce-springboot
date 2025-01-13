package com.lexuancong.location.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "district")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class District extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 450)
    private String name;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;


}
