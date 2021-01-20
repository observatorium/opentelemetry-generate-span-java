package io.observatorium.opentelemetry.generate.inventory;

import io.opentelemetry.context.Context;

public class OrderReceived {
    Context parent;

    public OrderReceived(Context parent) {
        this.parent = parent;
    }

    public Context getParentSpan() {
        return this.parent;
    }
}
