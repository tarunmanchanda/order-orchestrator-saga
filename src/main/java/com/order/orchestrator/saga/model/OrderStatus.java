package com.order.orchestrator.saga.model;

public enum OrderStatus {

    INITIATED("Initiated"), COMPLETED("Completed"), FAILED("Failed");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
