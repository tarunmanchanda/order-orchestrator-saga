package com.order.orchestrator.saga.handler;

import com.order.orchestrator.saga.core.BaseHandler;
import com.order.orchestrator.saga.model.Order;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CreateOrderHandler implements BaseHandler {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public Mono<ServerResponse> handleRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Order.class).flatMap(order -> {
            var orderCreated = producerTemplate.requestBody("direct:createOrder", order);
            return ServerResponse.ok().body(Mono.just(orderCreated), Order.class);
        }).onErrorResume(err -> ServerResponse.badRequest().contentType(MediaType.TEXT_PLAIN).bodyValue(err.getCause().getMessage()));
    }
}
