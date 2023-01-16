package com.order.orchestrator.saga.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {

    private String firstName;

    private String lastName;

    private String confirmationNumber;

    private String lengthOfStay;
}
