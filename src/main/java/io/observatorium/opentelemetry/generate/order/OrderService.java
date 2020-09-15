package io.observatorium.opentelemetry.generate.order;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;

import io.observatorium.opentelemetry.generate.Pause;
import io.observatorium.opentelemetry.generate.account.AccountService;
import io.observatorium.opentelemetry.generate.inventory.InventoryService;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;

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
    private Event<OrderSubmitted> orderSubmittedEvent;

    public void submit(Span parent) {
        Span span = tracer.spanBuilder("submit").setParent(parent).startSpan();
        span.setAttribute("order-id", "c85b7644b6b5");

        chargeCreditCard(span);
        orderSubmittedEvent.fireAsync(new OrderSubmitted(span));
        span.end();
    }

    public void chargeCreditCard(Span parent) {
        Span span = tracer.spanBuilder("chargeCreditCard").setParent(parent).startSpan();
        Pause.forDuration(TimeUnit.SECONDS, 1);
        span.setAttribute("card", "x123");
        span.end();
    }

    public void changeOrderStatus(@ObservesAsync OrderSubmitted event) {
        Span span = tracer.spanBuilder("changeOrderStatus").setParent(event.getParentSpan()).startSpan();
        Pause.forSomeTime();
        span.end();
    }

    public void dispatchEventToInventory(@ObservesAsync OrderSubmitted event) {
        Span span = tracer.spanBuilder("dispatchEventToInventory").setParent(event.getParentSpan()).startSpan();
        Pause.forSomeTime();
        inventoryService.processOrder(span);
        span.end();
    }
}
