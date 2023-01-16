package com.order.orchestrator.saga.service;

import com.order.orchestrator.saga.model.Order;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PaymentService {

    private final Map<String, Double> customerPaymentAccount = new ConcurrentHashMap<>();
    public PaymentService() {
        customerPaymentAccount.put("1000", 1000D);
        customerPaymentAccount.put("2000", 1000D);
        customerPaymentAccount.put("3000", 1000D);
    }

    public Order makePayment(Exchange exchange) {
        Order order = exchange.getMessage().getBody(Order.class);
        String customerAccountNumber = order.getPayment().getCustomerAccountNumber();
        if (customerPaymentAccount.containsKey(customerAccountNumber)) {
            Double currentBalance = customerPaymentAccount.get(customerAccountNumber);
            if (currentBalance < order.getLineItem().getTotalPrice()) {
                throw new RuntimeException("Insufficient Funds, Please try adding more funds to your account !!");
            }
            Double updatedAccountBalance = currentBalance - order.getLineItem().getTotalPrice();
            customerPaymentAccount.put(customerAccountNumber, updatedAccountBalance);
            order.getPayment().setCurrentCardBalance(updatedAccountBalance);
            return order;
        }
        return order;
    }

    public Order reversePayment(Exchange exchange) {
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
