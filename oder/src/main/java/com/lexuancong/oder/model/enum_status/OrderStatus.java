package com.lexuancong.oder.model.enum_status;

public enum OrderStatus {
    // Đơn hàng đã được tạo nhưng chưa được thanh toán hoặc xác nhận.
    PENDING("PENDING"),
    // Đơn hàng đã được xác nhận bởi hệ thống hoặc người bán.
    CONFIRMED("CONFIRMED"),
    // Đơn hàng đang được xử lý (chuẩn bị hàng, đóng gói,...)
    PROCESSING("PROCESSING"),
    //  Đơn hàng đã được đóng gói và sẵn sàng giao.
    PACKAGED("PACKAGED"),
    // Đơn hàng đã được giao cho đơn vị vận chuyển.
    SHIPPED("SHIPPED"),
    // Đơn hàng đang trên đường đến khách hàng.
    OUT_FOR_DELIVERY(("OUT_FOR_DELIVERY")),
    //  Khách hàng đã nhận hàng thành công.
    DELIVERED("DELIVERED"),
    //  Đơn hàng bị hủy do khách hoặc hệ thống.
    CANCELLED("CANCELLED"),
    // Đơn hàng đã được trả lại.
    RETURNED("RETURNED"),
    //  Khách hàng nhận lại tiền hoàn trả.
    REFUNDED("REFUNDED"),
    // Đơn hàng thất bại (ví dụ: lỗi thanh toán).
    FAILED("FAILED"),
//    Khách yêu cầu trả hàng.
    RETURN_REQUESTED("RETURN_REQUESTED");



    private final String name;
    private OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {return name;}



}
