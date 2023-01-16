package com.order.orchestrator.saga.routes;

import com.order.orchestrator.saga.model.Order;
import com.order.orchestrator.saga.model.OrderStatus;
import com.order.orchestrator.saga.service.CreateOrderService;
import com.order.orchestrator.saga.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateOrderSagaRoute extends RouteBuilder {

    private final CreateOrderService createOrderService;

    private final PaymentService paymentService;

    @Override
    public void configure() throws Exception {

        getContext().addService(new InMemorySagaService());

        from("direct:createOrder")
                .process(exchange -> {
                    exchange.getMessage().setHeader("id", UUID.randomUUID().toString());
                    Order order = exchange.getMessage().getBody(Order.class);
                    order.setOrderStatus(OrderStatus.INITIATED.getValue());
                    exchange.getMessage().setBody(order);
                })
                .log(LoggingLevel.INFO, "ID:${header.id}", "Reservation Received: ${body}")
                .saga()
                .to("direct:holdPayment")
                .to("direct:makeReservation");
                //.to("direct:saveReservation");

        from("direct:holdPayment")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .option("id", header("id"))
                .option("body", body())
                .compensation("direct:reversePayment")
                .bean(paymentService, "makePayment")
                .log("ID: ${header.id}, Payment made for OrderId: ${header.id}, currentAccountBalance: ${body.payment.currentCardBalance}");


        from("direct:makeReservation")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .option("id", header("id"))
                .option("body", body())
                .compensation("direct:cancelReservation")
                .bean(createOrderService, "makeReservation")
                .log("ID: ${header.id}, Reservation booked with ${body}");


        from("direct:reversePayment")
                .bean(paymentService, "reversePayment")
                .log("ID: ${header.id}, Payment reversed for OrderId: ${header.id}, currentAccountBalance: ${body.payment.currentCardBalance}");


        from("direct:cancelReservation")
                .bean(createOrderService, "cancelReservation")
                .log("ID: ${header.id}, Reservation cancelled ${body}");


        from("direct:saveReservation")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .option("id", header("id"))
                .setBody(body())
                .compensation("direct:updateReservation")
                .bean(createOrderService, "saveReservationToDB");
    }
}
