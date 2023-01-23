package com.order.orchestrator.saga.workflowstep;

import com.order.orchestrator.saga.core.WorkflowStep;
import com.order.orchestrator.saga.model.Order;
import com.order.orchestrator.saga.model.OrderStatus;
import com.order.orchestrator.saga.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderWorkflowStep implements WorkflowStep<Order> {

    private final OrderRepository orderRepository;

    @Override
    public Order process(Exchange exchange) {
        Order order = exchange.getMessage().getBody(Order.class);
        order.setId(exchange.getMessage().getHeader("id", UUID.class));
        order.setOrderStatus(OrderStatus.COMPLETED.getValue());
        if (order.getLineItem().getLengthOfStay() > 10) {
            throw new RuntimeException("Room Sold Out");
        }
        orderRepository.saveOrder(order);
        return order;
    }

    @Override
    public Order revert(Exchange exchange) {
        Order order = exchange.getIn().getHeader("body", Order.class);
        order.setOrderStatus("Cancelled");
        orderRepository.saveOrder(order);
        return order;
    }
}
