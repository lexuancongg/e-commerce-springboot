package com.lexuancong.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class Stock extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // khóa ngoại sp
    @Column(nullable = false)
    private Long productId;

    private int quantity;

    // số lượng đang pending để xử lý cho đơn hàng
    private int lockedQuantity;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;


}
