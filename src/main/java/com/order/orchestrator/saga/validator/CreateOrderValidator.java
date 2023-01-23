package com.order.orchestrator.saga.validator;

import com.order.orchestrator.saga.core.BaseValidator;
import com.order.orchestrator.saga.model.Order;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderValidator implements BaseValidator<Order> {

    @Override
    public boolean validate(Order order) {
        return true;
    }
}
