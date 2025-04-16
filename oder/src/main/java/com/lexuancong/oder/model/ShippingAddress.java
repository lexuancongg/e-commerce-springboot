package com.lexuancong.oder.model;

import jakarta.persistence.*;
import lombok.*;

// địa chỉ đơn hàng giao tới
@Entity
@Table(name = "shipping_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String phoneNumber;
    private String specificAddress;
    private Long districtId;
    private Long provinceId;
    private Long countyId;
}
