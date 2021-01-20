package io.observatorium.opentelemetry.generate.order;

import io.opentelemetry.context.Context;

public class OrderSubmitted {
    Context ctx;

    public OrderSubmitted(Context ctx) {
        this.ctx = ctx;
    }

    public Context getContext() {
        return this.ctx;
    }
}
