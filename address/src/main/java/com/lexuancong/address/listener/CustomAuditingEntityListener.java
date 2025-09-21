package com.lexuancong.address.listener;

import com.lexuancong.address.model.BaseAuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
// inject dep dù nó không phải là bean mà dùng new thủ công
@Configurable
public class CustomAuditingEntityListener extends AuditingEntityListener {
    public CustomAuditingEntityListener( ObjectFactory<AuditingHandler> handler) {
        super.setAuditingHandler(handler);
    }

    @PrePersist
    public void touchForCreate(Object target) {
        BaseAuditEntity baseAuditEntity = (BaseAuditEntity) target;
        if(baseAuditEntity.getCreatedBy() == null){
            super.touchForCreate(target);
        }else {
            if(baseAuditEntity.getLastUpdatedBy() == null){
                baseAuditEntity.setLastUpdatedBy(baseAuditEntity.getCreatedBy());
            }
        }
    }

    @PreUpdate
    public void touchForUpdate(Object target) {
        BaseAuditEntity baseAuditEntity = (BaseAuditEntity) target;
        if(baseAuditEntity.getLastUpdatedBy() == null){
            super.touchForUpdate(target);
        }


    }

}

// docs :
// jpa không liên quan tới spring, nó không lấy auditentity.. từ bean trong containerContex để dùng mà tự new ra nên tạo bean không có tác dụng gì

