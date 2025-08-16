package com.lexuancong.search.kafka.cdc.message;

public enum Operation {
    CREATE("c"),
    UPDATE("u"),
    DELETE("d"),
    READ("r");

    private String action;
    private Operation(String action) {
        this.action = action;
    }
}
