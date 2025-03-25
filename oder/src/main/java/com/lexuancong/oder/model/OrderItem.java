package com.lexuancong.oder.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private Long productId ;
    private int quantity ;
    private Long orderId ;
    @Column(name = "price")
    private BigDecimal productPrice ;
    private BigDecimal totalPrice ;
    @Column(name = "order_id")
    private String oderId;
    @ManyToOne(fetch = FetchType.LAZY)
    // cả order và oderId đều ánh xạ tới cột oder_id , insertable = false,updatable = false giúp hibernate khoongc inser, update cột oder_id dựa trên obj order
    @JoinColumn(name = "order_id", insertable = false,updatable = false)
    private Order order ;

}
