package com.lexuancong.customer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// chúng ta tách riêng ra thay vì để userID trong bbangr address vì isActive là thuộc tính metadata => nó là đặc tính của mối quan hệ giua user và address chứ không phải riêng của bảng nào cả
// => sau này mở rộng thêm được
public class UserAddress extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private Long addressId;
    private boolean isActive;

}
