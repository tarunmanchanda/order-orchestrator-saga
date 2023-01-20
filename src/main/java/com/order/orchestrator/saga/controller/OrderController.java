//package com.order.orchestrator.saga.controller;
//
//import com.order.orchestrator.saga.model.Order;
//import com.order.orchestrator.saga.routes.CreateOrderSagaRoute;
//import lombok.RequiredArgsConstructor;
//import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Configuration
//@RequiredArgsConstructor
//public class OrderController {
//
//    private final CreateOrderSagaRoute sagaRoute;
//
//    @Bean
//    public RouterFunction<ServerResponse> getOrder() {
//        return RouterFunctions.route(RequestPredicates.GET("/v1/orders"), sagaRoute::getAllOrders);
//    }
//
//    @Bean
//    public RouterFunction<ServerResponse> createOrder() {
//        return RouterFunctions.route(RequestPredicates.POST("/v1/orders"), sagaRoute::createOrder);
//    }
//
//}
