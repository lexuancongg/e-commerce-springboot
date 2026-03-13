package com.lexuancong.oder.model;

import com.lexuancong.oder.model.enum_status.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String note;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)

    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private ShippingAddress shippingAddress;
    private int numberItem;
    private BigDecimal totalPrice;

    private String customerId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus oderStatus;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;


    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;


    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod PaymentMethod;


    private Long paymentId;

    private Long checkoutId;







}
