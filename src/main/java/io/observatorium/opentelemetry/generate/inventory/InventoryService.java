package io.observatorium.opentelemetry.generate.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;

import io.observatorium.opentelemetry.generate.Pause;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

@ApplicationScoped
public class InventoryService {
    @Inject
    @Inventory
    Tracer tracer;

    @Inject
    Event<OrderReceived> orderReceivedEvent;

    public void processOrder(Context parent) {
        Span span = tracer.spanBuilder("processOrder").setParent(parent).startSpan();
        Pause.forSomeTime();
        orderReceivedEvent.fireAsync(new OrderReceived(parent));
        span.end();
    }

    public void checkInventoryStatus(@ObservesAsync OrderReceived event) {
        Span span = tracer.spanBuilder("checkInventoryStatus").setParent(event.getParentSpan()).startSpan();
        Pause.forSomeTime();
        span.end();
    }

    public void updateInventory(@ObservesAsync OrderReceived event) {
        Span span = tracer.spanBuilder("updateInventory").setParent(event.getParentSpan()).startSpan();
        Pause.forSomeTime();
        span.setStatus(StatusCode.ERROR, "Cannot open connection to storage. Queueing update.");
        span.end();
    }

    public void prepareOrderManifest(@ObservesAsync OrderReceived event) {
        Span span = tracer.spanBuilder("prepareOrderManifest").setParent(event.getParentSpan()).startSpan();
        Pause.forSomeTime();
        span.end();
    }

}
