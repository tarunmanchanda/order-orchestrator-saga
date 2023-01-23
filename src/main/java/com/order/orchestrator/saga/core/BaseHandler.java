package com.order.orchestrator.saga.core;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface BaseHandler {

    Mono<ServerResponse> handleRequest(ServerRequest serverRequest);
}
