package io.observatorium.opentelemetry.generate.account;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class TracerInitializer {
    OpenTelemetrySdk sdkProvider;

    @Produces
    @Account
    Tracer tracer;

    void onStart(@Observes StartupEvent ev) {
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder().setTimeout(2, TimeUnit.SECONDS).build();
        BatchSpanProcessor spanProcessor = BatchSpanProcessor.builder(spanExporter)
                .setScheduleDelay(100, TimeUnit.MILLISECONDS).build();

        sdkProvider = OpenTelemetrySdk.builder()
                .setTracerProvider(SdkTracerProvider.builder().addSpanProcessor(spanProcessor)
                        .setResource(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "account")))
                        .build())
                .build();

        tracer = sdkProvider.getTracer("account");
    }

    void onStop(@Observes ShutdownEvent ev) {
        // shutdown for processors and exporters should be called as a result of
        // shutting down the tracing provider
        sdkProvider.getSdkTracerProvider().shutdown();
    }
}
