package com.order.orchestrator.saga.processor;

import com.order.orchestrator.saga.model.Order;
import com.order.orchestrator.saga.model.OrderStatus;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateOrderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getMessage().setHeader("id", UUID.randomUUID().toString());
        Order order = exchange.getMessage().getBody(Order.class);
        order.setOrderStatus(OrderStatus.INITIATED.getValue());
        exchange.getMessage().setBody(order);
    }
}
