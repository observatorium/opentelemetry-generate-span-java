package io.observatorium.opentelemetry.generate.order;

import io.opentelemetry.trace.Span;

public class OrderSubmitted {
    Span parent;

    public OrderSubmitted(Span parent) {
        this.parent = parent;
    }

    public Span getParentSpan() {
        return this.parent;
    }
}
