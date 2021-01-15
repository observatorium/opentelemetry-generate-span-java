package io.observatorium.opentelemetry.generate.order;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.observatorium.opentelemetry.generate.account.AccountService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

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
        Context ctx = span.storeInContext(Context.root());

        String account = accountService.get(ctx);
        span.setAttribute("account", account);

        orderService.submit(ctx);

        span.end();
        return "Created";
    }
}
