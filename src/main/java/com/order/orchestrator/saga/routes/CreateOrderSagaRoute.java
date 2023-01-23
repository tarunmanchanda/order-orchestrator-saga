package com.order.orchestrator.saga.routes;

import com.order.orchestrator.saga.processor.CreateOrderProcessor;
import com.order.orchestrator.saga.workflowstep.CreateOrderWorkflowStep;
import com.order.orchestrator.saga.workflowstep.PaymentWorkflowStep;
import com.order.orchestrator.saga.workflowstep.ValidatorWorkflowStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderSagaRoute extends RouteBuilder {

    private final CreateOrderWorkflowStep createOrderWorkflowStep;

    private final PaymentWorkflowStep paymentWorkflowStep;

    private final ValidatorWorkflowStep validatorWorkflowStep;

    @Override
    public void configure() throws Exception {

        getContext().addService(new InMemorySagaService());

        onException(RuntimeException.class)
                .log(LoggingLevel.ERROR, "Reservation Booking failed for reason: ${exception.message}")
                .handled(false);

        from("direct:createOrder")
                .process(new CreateOrderProcessor())
                .end()
                .log(LoggingLevel.INFO, "ID:${header.id}", "Reservation Received: ${body}")
                .saga()
                .to("direct:validate")
                .to("direct:holdPayment")
                .to("direct:makeReservation");

        from("direct:validate")
                .saga()
                .option("id", header("id"))
                .option("body", body())
                .bean(validatorWorkflowStep, "process")
                .choice()
                .when(exchange -> exchange.getMessage().getBody() != null)
                .log("ID: ${header.id}, Validation Successful");

        from("direct:holdPayment")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .option("id", header("id"))
                .option("body", body())
                .compensation("direct:reversePayment")
                .bean(paymentWorkflowStep, "process")
                .log("ID: ${header.id}, Payment made for OrderId: ${header.id}, currentAccountBalance: ${body.payment.currentCardBalance}");

        from("direct:reversePayment")
                .bean(paymentWorkflowStep, "revert")
                .log("ID: ${header.id}, Payment reversed for OrderId: ${header.id}, currentAccountBalance: ${body.payment.currentCardBalance}");

        from("direct:makeReservation")
                .saga()
                .propagation(SagaPropagation.MANDATORY)
                .option("id", header("id"))
                .option("body", body())
                .compensation("direct:cancelReservation")
                .bean(createOrderWorkflowStep, "process")
                .log("ID: ${header.id}, Reservation booked with ${body}");

        from("direct:cancelReservation")
                .bean(createOrderWorkflowStep, "revert")
                .log("ID: ${header.id}, Reservation cancelled ${body}");
    }

}
