package com.order.orchestrator.saga.service;

import com.order.orchestrator.saga.model.Order;
import com.order.orchestrator.saga.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository orderRepository;

    public Order makeReservation(Exchange exchange) {
        Order order = exchange.getMessage().getBody(Order.class);
        order.setId(exchange.getMessage().getHeader("id", UUID.class));
        order.setOrderStatus("Confirmed");
        if (order.getPayment().getCurrentCardBalance() < 500) {
            throw new RuntimeException("Reservation System failed to create the reservation");
        }
        return order;
    }

    public Order cancelReservation(Exchange exchange) {
        Order order = exchange.getIn().getHeader("body", Order.class);
        order.setOrderStatus("Cancelled");
        return order;
    }

    public Order saveReservationToDB(Exchange exchange) {
        Order order = exchange.getIn().getBody(Order.class);
        log.info("Reservation Saved to Database for OrderId: {}", order.getId().toString());
        return order;
    }
}
