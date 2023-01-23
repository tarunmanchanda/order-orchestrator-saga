package com.order.orchestrator.saga.workflowstep;

import com.order.orchestrator.saga.core.WorkflowStep;
import com.order.orchestrator.saga.model.Order;
import com.order.orchestrator.saga.validator.CreateOrderValidator;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ValidatorWorkflowStep implements WorkflowStep<Order> {

    private final CreateOrderValidator createOrderValidator;

    @Override
    public Order process(Exchange exchange) {
        //validate Order
        Order order = exchange.getMessage().getBody(Order.class);
        if (createOrderValidator.validate(order)) {
            return order;
        }
        throw new RuntimeException("Validation for incoming request failed");
    }

    @Override
    public Order revert(Exchange exchange) {
        return null;
    }
}
