package com.order.orchestrator.saga.workflowstep;

import com.order.orchestrator.saga.core.WorkflowStep;
import com.order.orchestrator.saga.model.Order;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PaymentWorkflowStep implements WorkflowStep<Order> {

    private final Map<String, Double> customerPaymentAccount = new ConcurrentHashMap<>();

    public PaymentWorkflowStep() {
        customerPaymentAccount.put("1000", 1000D);
        customerPaymentAccount.put("2000", 1000D);
        customerPaymentAccount.put("3000", 1000D);
    }

    @Override
    public Order process(Exchange exchange) {
        Order order = exchange.getMessage().getBody(Order.class);
        String customerAccountNumber = order.getPayment().getCustomerAccountNumber();
        if (customerPaymentAccount.containsKey(customerAccountNumber)) {
            Double currentBalance = customerPaymentAccount.get(customerAccountNumber);
            Double updatedAccountBalance = currentBalance - order.getLineItem().getTotalPrice();
            customerPaymentAccount.put(customerAccountNumber, updatedAccountBalance);
            order.getPayment().setCurrentCardBalance(updatedAccountBalance);
            return order;
        }
        return order;
    }

    @Override
    public Order revert(Exchange exchange) {
        Order order = exchange.getIn().getHeader("body", Order.class);
        String customerAccountNumber = order.getPayment().getCustomerAccountNumber();

        if (customerPaymentAccount.containsKey(customerAccountNumber)) {
            Double currentBalance = customerPaymentAccount.get(customerAccountNumber);
            Double updatedAccountBalance = currentBalance + order.getLineItem().getTotalPrice();
            customerPaymentAccount.put(customerAccountNumber, updatedAccountBalance);
            order.getPayment().setCurrentCardBalance(updatedAccountBalance);
            return order;
        }
        return order;
    }
}
