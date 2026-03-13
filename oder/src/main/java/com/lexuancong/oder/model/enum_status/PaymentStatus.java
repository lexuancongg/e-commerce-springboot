package com.lexuancong.oder.model.enum_status;

public enum PaymentStatus {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED");
    private String name;
    PaymentStatus(String name) {
        this.name = name;
    }




}
