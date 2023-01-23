package com.order.orchestrator.saga.core;

import org.apache.camel.Exchange;

public interface WorkflowStep<T> {

    T process(Exchange exchange);

    T revert(Exchange exchange);
}
