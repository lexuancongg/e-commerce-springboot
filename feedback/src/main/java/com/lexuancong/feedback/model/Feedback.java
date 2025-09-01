package com.lexuancong.feedback.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private int star;

    private Long productId;



//    // hai thuôc tính người dùng  của keycloak =>  nếu lưu như này thì nó cố định k update theo

    private String lastName;

    private String firstName;
//    private String customerId;

}
