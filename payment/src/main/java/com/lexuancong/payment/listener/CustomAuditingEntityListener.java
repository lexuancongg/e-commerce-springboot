package com.lexuancong.payment.listener;

import com.lexuancong.payment.model.AuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
// inject dep  mà container bean không quản lý
@Configurable
public class CustomAuditingEntityListener extends AuditingEntityListener {
    public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> handler) {
        super.setAuditingHandler(handler);

    }
    @Override
    @PrePersist
    public void touchForCreate(Object target) {
        AuditEntity entity = (AuditEntity) target;
        if(entity.getCreatedBy() == null){
            super.touchForCreate(target);
        }else {
            if(entity.getLastUpdatedBy() == null){
                entity.setLastUpdatedBy(entity.getCreatedBy());
            }

        }
    }

    @Override
    @PreUpdate
    public void touchForUpdate(Object target) {
        AuditEntity entity = (AuditEntity) target;
        if(entity.getLastUpdatedBy() == null){
            super.touchForUpdate(target);
        }
    }

}
