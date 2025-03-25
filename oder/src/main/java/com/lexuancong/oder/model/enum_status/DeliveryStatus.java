package com.lexuancong.oder.model.enum_status;
// Trạng thái giao hàng
public enum DeliveryStatus {
    // Đang chờ lấy hàng từ kho hoặc nhà cung cấp.
    PENDING_PICKUP("PENDING_PICKUP"),
    // 	Đơn hàng đang trên đường vận chuyển.
    IN_TRANSIT("IN_TRANSIT"),
    // Đang giao hàng đến khách.
    OUT_FOR_DELIVERY("OUT_FOR_DELIVERY"),
    // Đã giao hàng thành công cho khách.
    DELIVERED("DELIVERED"),
    // Giao hàng thất bại (khách vắng mặt, sai địa chỉ, từ chối nhận hàng).
    FAILED_DELIVERY("FAILED_DELIVERY"),
    // Khách yêu cầu trả hàng.
    RETURN_REQUESTED("RETURN_REQUESTED"),
    // Đơn hàng đã hoàn về kho.
    RETURNED("RETURNED");
    private final String name;
    DeliveryStatus(String name) {
        this.name = name;
    }
}
