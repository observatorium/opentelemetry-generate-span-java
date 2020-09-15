package io.observatorium.opentelemetry.generate.inventory;

import io.opentelemetry.trace.Span;

public class OrderReceived {
    Span parent;

    public OrderReceived(Span parent) {
        this.parent = parent;
    }

    public Span getParentSpan() {
        return this.parent;
    }
}
