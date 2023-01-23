package com.order.orchestrator.saga.processor;

import com.order.orchestrator.saga.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class RetrieveOrderProcessor implements Processor {
    private final OrderRepository orderRepository;

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getMessage().setBody(Flux.fromIterable(orderRepository.getAllOrders()));
    }
}
