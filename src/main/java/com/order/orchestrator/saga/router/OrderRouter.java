package com.order.orchestrator.saga.router;

import com.order.orchestrator.saga.handler.CreateOrderHandler;
import com.order.orchestrator.saga.handler.RetrieveOrderHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class OrderRouter {

    private final CreateOrderHandler createOrderHandler;

    private final RetrieveOrderHandler retrieveOrderHandler;

    @Bean
    public RouterFunction<ServerResponse> getOrder() {
        return RouterFunctions.route(RequestPredicates.GET("/v1/orders"), retrieveOrderHandler::handleRequest);
    }

    @Bean
    public RouterFunction<ServerResponse> createOrder() {
        return RouterFunctions.route(RequestPredicates.POST("/v1/orders"), createOrderHandler::handleRequest);
    }
}
