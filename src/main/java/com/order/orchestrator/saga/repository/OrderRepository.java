package com.order.orchestrator.saga.repository;

import com.order.orchestrator.saga.model.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderRepository {

    private final Map<UUID, Order> cacheMap = new ConcurrentHashMap<>();

    public void saveOrder(Order order) {
        cacheMap.put(order.getId(), order);
    }
}
