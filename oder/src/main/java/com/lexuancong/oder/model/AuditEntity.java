package com.lexuancong.oder.model;

import com.lexuancong.oder.listener.CustomAuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
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
@EntityListeners(CustomAuditingEntityListener.class)
public class AuditEntity {
    @CreationTimestamp
    // no change value when saved in db
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    private String  createdBy;

    @UpdateTimestamp
    private ZonedDateTime lastUpdatedAt;
    private String  lastUpdatedBy;
}
