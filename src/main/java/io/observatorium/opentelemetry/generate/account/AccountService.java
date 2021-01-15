package io.observatorium.opentelemetry.generate.account;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.observatorium.opentelemetry.generate.Pause;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

@ApplicationScoped
public class AccountService {
    @Inject
    @Account
    Tracer tracer;

    public String get(Context parent) {
        Span span = tracer.spanBuilder("get").setParent(parent).startSpan();
        Context ctx = span.storeInContext(parent);
        Pause.forSomeTime();
        try {
            String accountFromCache = getAccountFromCache(ctx);
            if (null == accountFromCache) {
                // get account from storage
                return getAccountFromStorage(ctx);
            }
        } finally {
            span.end();
        }
        return null;
    }

    public String getAccountFromCache(Context ctx) {
        Span span = tracer.spanBuilder("getAccountFromCache").setParent(ctx).startSpan();
        Pause.forSomeTime();
        span.setStatus(StatusCode.ERROR, "cache miss");
        span.end();
        return null;
    }

    public String getAccountFromStorage(Context ctx) {
        Span span = tracer.spanBuilder("getAccountFromStorage").setParent(ctx).startSpan();
        Pause.forSomeTime();
        String account = UUID.randomUUID().toString();
        span.end();
        return account;
    }
}
