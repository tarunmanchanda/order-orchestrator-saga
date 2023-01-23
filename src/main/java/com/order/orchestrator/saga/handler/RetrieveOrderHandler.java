package com.order.orchestrator.saga.handler;

import com.order.orchestrator.saga.core.BaseHandler;
import com.order.orchestrator.saga.model.Order;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RetrieveOrderHandler implements BaseHandler {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public Mono<ServerResponse> handleRequest(ServerRequest serverRequest) {
        var orders = producerTemplate.requestBody("direct:getOrders", serverRequest);
        return ServerResponse.ok().body(orders, Order.class);
    }
}
