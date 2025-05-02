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
public class Rating  extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private int star;

    private Long productId;



    // hai thuôc tính người dùng  của keycloak

    private String lastName;

    private String firstName;

}
