package com.lexuancong.customer.model;

import com.lexuancong.customer.listener.CustomAuditingEntityListener;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(CustomAuditingEntityListener.class)
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
