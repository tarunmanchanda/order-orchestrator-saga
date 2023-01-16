package com.order.orchestrator.saga.routes;

import com.order.orchestrator.saga.model.Order;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;

@Component
public class OrderRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .apiProperty("api.title", "Saga Order Orchestrator")
                .apiProperty("api.version", "1.0");

        rest().consumes("application/json").produces("application/json")
                .post("/v1/orders")
                .responseMessage("201", "Order Created")
                .description("Create a new Order")
                .type(Order.class)
                .param().name("body").type(body).description("Order Payload")
                .endParam()
                .to("direct:createOrder")
                .outType(Order.class);
    }
}
