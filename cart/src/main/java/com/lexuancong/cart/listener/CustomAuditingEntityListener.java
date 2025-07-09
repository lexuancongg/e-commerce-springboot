package com.lexuancong.cart.listener;

import com.lexuancong.cart.model.BaseAuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Configurable
public class CustomAuditingEntityListener extends AuditingEntityListener {
    public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> handler) {
        super.setAuditingHandler(handler);
    }
    @Override
    @PrePersist
    public void touchForCreate(Object entity){
        // tham chiếu mới cùng t tới entity
        BaseAuditEntity baseAuditEntity = (BaseAuditEntity) entity;
        if(baseAuditEntity.getCreatedBy()==null){
            super.touchForCreate(entity);
        }else {
            if(baseAuditEntity.getLastUpdatedBy()==null){
                baseAuditEntity.setLastUpdatedBy(baseAuditEntity.getCreatedBy());
            }
        }

    }

    @Override
    @PreUpdate
    public void touchForUpdate(Object entity){
        if((((BaseAuditEntity) entity).getLastUpdatedBy()==null)){
            super.touchForUpdate(entity);
        }

    }

}
