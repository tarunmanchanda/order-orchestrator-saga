package com.order.orchestrator.saga.service;

import com.order.orchestrator.saga.model.Order;
import com.order.orchestrator.saga.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.List;
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
        if (order.getLineItem().getLengthOfStay() > 10) {
            throw new RuntimeException("Reservation Booking failed for reason: Room Sold Out");
        }
        orderRepository.saveOrder(order);
        return order;
    }

    public Order cancelReservation(Exchange exchange) {
        Order order = exchange.getIn().getHeader("body", Order.class);
        order.setOrderStatus("Cancelled");
        orderRepository.saveOrder(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }
}
