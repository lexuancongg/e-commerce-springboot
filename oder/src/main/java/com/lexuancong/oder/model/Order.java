package com.lexuancong.oder.model;

import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // email  khách hàng
    private String email;
    // ghi chú đơn hàng
    private String note;
    // ALL : bất kì thay đổi nào cuũng ảnh hưởng tới Address , khi oder được lưu thì address cũng được lưu
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    // 1 địa chỉ có một đơn hàng vì nếu 1 địa chỉ có nhiều đơn hàng thì khi thay đổi địa chỉ 1 đơn hàng thì tất cả đơn hàng b thay đổi
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private ShippingAddress shippingAddress;
    private int numberItem;
    private BigDecimal totalPrice;
    private String customerId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus oderStatus;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;





}
