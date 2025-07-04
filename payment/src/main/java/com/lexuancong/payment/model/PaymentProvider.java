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

public class PaymentProvider {
    @Id
    private String name;
    private boolean isEnabled;

    private String configureUrl;
    // mỗi provider có những thông tin khác nhau
    private String properties;

}
