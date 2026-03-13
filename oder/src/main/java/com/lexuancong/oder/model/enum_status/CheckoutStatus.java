package com.lexuancong.oder.model.enum_status;

public enum CheckoutStatus {
    PAID("PAID"),
    PENDING("PENDING");

    private final String name;

    CheckoutStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
