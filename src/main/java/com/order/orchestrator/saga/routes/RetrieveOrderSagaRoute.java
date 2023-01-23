package com.order.orchestrator.saga.routes;

import com.order.orchestrator.saga.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetrieveOrderSagaRoute extends RouteBuilder {

    private final OrderRepository orderRepository;

    @Override
    public void configure() throws Exception {
        from("direct:getOrders")
                .process("retrieveOrderProcessor")
                .end();
    }
}
