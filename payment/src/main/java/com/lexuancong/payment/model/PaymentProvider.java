package com.lexuancong.payment.model;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "payment_provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// xác định các nha thanh toan duoc tich hop vaf app sau nay
public class PaymentProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isEnabled;
    private String name;
    private String configureUrl;
    private String additionalSettings;

}
