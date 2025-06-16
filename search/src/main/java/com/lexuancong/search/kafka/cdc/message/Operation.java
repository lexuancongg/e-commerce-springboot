package com.lexuancong.search.kafka.cdc.message;

public enum Operation {
    CREATE("c"),
    UPDATE("u"),
    DELETE("d"),
    READ("r");

    private String actionName;
    private Operation(String actionName) {
        this.actionName = actionName;
    }
}
