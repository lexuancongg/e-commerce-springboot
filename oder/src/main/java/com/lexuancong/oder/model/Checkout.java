package com.lexuancong.oder.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lexuancong.oder.model.enum_status.CheckoutStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checkout")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Checkout extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // sau này gởi toong báo về email
    private String email;
    private  String note;


    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;


    private String customerId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CheckoutStatus checkoutStatus;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<CheckoutItem> checkoutItems = new ArrayList<>();









}
