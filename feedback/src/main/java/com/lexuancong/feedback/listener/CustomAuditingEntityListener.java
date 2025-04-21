package com.lexuancong.feedback.listener;

import com.lexuancong.feedback.model.AuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Configuration
public class CustomAuditingEntityListener extends AuditingEntityListener {
    public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> handler) {
        super.setAuditingHandler(handler);
    }

    @Override
    @PrePersist
    public void touchForCreate(Object entity) {
        AuditEntity auditEntity = (AuditEntity) entity;
        if(auditEntity.getCreatedBy()==null){
            super.touchForCreate(entity);
        }else{
            if(auditEntity.getLastUpdatedBy()==null){
                auditEntity.setLastUpdatedBy(auditEntity.getCreatedBy());
            }
        }
    }

    @Override
    @PreUpdate
    public void touchForUpdate(Object entity) {
        AuditEntity auditEntity = (AuditEntity) entity;
        if (auditEntity.getLastUpdatedBy() == null) {
            super.touchForUpdate(entity);
        }
    }

}
