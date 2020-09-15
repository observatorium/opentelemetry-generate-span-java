package io.observatorium.opentelemetry.generate.order;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.observatorium.opentelemetry.generate.account.AccountService;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;

@Path("/order")
public class Endpoint {

    @Inject
    OrderService orderService;

    @Inject
    AccountService accountService;

    @Inject
    @Order
    Tracer tracer;

    @GET // in the real world, we'd have a post here
    public String create() {
        Span span = tracer.spanBuilder("createOrder").startSpan();

        String account = accountService.get(span);
        span.setAttribute("account", account);

        orderService.submit(span);

        span.end();
        return "Created";
    }
}
