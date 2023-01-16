package com.order.orchestrator.saga.routes;

import com.order.orchestrator.saga.model.Order;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateOrderSagaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        getContext().addService(new InMemorySagaService());

        from("direct:createOrder")
                .process(exchange -> {
                    exchange.getMessage().setHeader("id", UUID.randomUUID());
                    Order order = exchange.getMessage().getBody(Order.class);
                    order.setId(exchange.getMessage().getHeader("id", UUID.class));
                    exchange.getMessage().setBody(order);
                })
                .log(LoggingLevel.INFO, "ID:${header.id}", "Order Received: ${body}");
    }
}
