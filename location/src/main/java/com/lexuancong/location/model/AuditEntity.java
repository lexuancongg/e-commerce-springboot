package com.lexuancong.location.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter

public class AuditEntity {
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @UpdateTimestamp
    private ZonedDateTime lastUpdatedAt;

    @LastModifiedBy
    private String lastUpdatedBy;

}
