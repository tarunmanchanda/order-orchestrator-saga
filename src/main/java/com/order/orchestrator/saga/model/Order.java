package com.order.orchestrator.saga.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID id;

    private String orderStatus;

    private LineItem lineItem;

    private Payment payment;
}

