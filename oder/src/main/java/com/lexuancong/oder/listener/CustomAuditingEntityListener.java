package com.lexuancong.oder.listener;

import com.lexuancong.oder.model.AuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// ho trọ tiêm dep mà spring container không quản lý
@Configurable
public class CustomAuditingEntityListener extends AuditingEntityListener {
    public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> handler) {
        super.setAuditingHandler(handler);
    }
    @Override
    @PrePersist
    public void touchForCreate(Object entity){
        // tham chiếu mới cùng t tới entity
        AuditEntity auditEntity = (AuditEntity) entity;
        if(auditEntity.getCreatedBy()==null){
            super.touchForCreate(entity);
        }else {
            if(auditEntity.getLastUpdatedBy()==null){
                auditEntity.setLastUpdatedBy(auditEntity.getLastUpdatedBy());
            }
        }

    }

    @Override
    @PreUpdate
    public void touchForUpdate(Object entity){
        if((((AuditEntity) entity).getLastUpdatedBy()==null)){
            super.touchForUpdate(entity);
        }

    }

}
