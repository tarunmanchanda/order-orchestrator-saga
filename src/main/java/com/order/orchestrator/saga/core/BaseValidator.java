package com.order.orchestrator.saga.core;

public interface BaseValidator<T> {

    boolean validate(T t);
}
