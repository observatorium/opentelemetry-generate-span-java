package io.observatorium.opentelemetry.generate.order;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;

import io.observatorium.opentelemetry.generate.Pause;
import io.observatorium.opentelemetry.generate.account.AccountService;
import io.observatorium.opentelemetry.generate.inventory.InventoryService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

@ApplicationScoped
public class OrderService {

    @Inject
    AccountService accountService;

    @Inject
    InventoryService inventoryService;

    @Inject
    @Order
    Tracer tracer;

    @Inject
    Event<OrderSubmitted> orderSubmittedEvent;

    public void submit(Context parent) {
        Span span = tracer.spanBuilder("submit").setParent(parent).startSpan();
        Context ctx = span.storeInContext(parent);
        span.setAttribute("order-id", "c85b7644b6b5");

        chargeCreditCard(ctx);
        orderSubmittedEvent.fireAsync(new OrderSubmitted(ctx));
        span.end();
    }

    public void chargeCreditCard(Context parent) {
        Span span = tracer.spanBuilder("chargeCreditCard").setParent(parent).startSpan();
        Pause.forDuration(TimeUnit.SECONDS, 1);
        span.setAttribute("card", "x123");
        span.end();
    }

    public void changeOrderStatus(@ObservesAsync OrderSubmitted event) {
        Span span = tracer.spanBuilder("changeOrderStatus").setParent(event.getContext()).startSpan();
        Pause.forSomeTime();
        span.end();
    }

    public void dispatchEventToInventory(@ObservesAsync OrderSubmitted event) {
        Span span = tracer.spanBuilder("dispatchEventToInventory").setParent(event.getContext()).startSpan();
        Context ctx = span.storeInContext(event.getContext());
        Pause.forSomeTime();
        inventoryService.processOrder(ctx);
        span.end();
    }
}
