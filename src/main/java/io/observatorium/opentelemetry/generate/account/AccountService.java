package io.observatorium.opentelemetry.generate.account;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.observatorium.opentelemetry.generate.Pause;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Status;
import io.opentelemetry.trace.Tracer;

@ApplicationScoped
public class AccountService {
    @Inject
    @Account
    Tracer tracer;

    public String get(Span parent) {
        Span span = tracer.spanBuilder("get").setParent(parent).startSpan();
        Pause.forSomeTime();
        try {
            String accountFromCache = getAccountFromCache(span);
            if (null == accountFromCache) {
                // get account from storage
                return getAccountFromStorage(span);
            }
        } finally {
            span.end();
        }
        return null;
    }

    public String getAccountFromCache(Span parent) {
        Span span = tracer.spanBuilder("getAccountFromCache").setParent(parent).startSpan();
        Pause.forSomeTime();
        span.setStatus(Status.NOT_FOUND.withDescription("cache miss"));
        span.end();
        return null;
    }

    public String getAccountFromStorage(Span parent) {
        Span span = tracer.spanBuilder("getAccountFromStorage").setParent(parent).startSpan();
        Pause.forSomeTime();
        String account = UUID.randomUUID().toString();
        span.end();
        return account;
    }
}
