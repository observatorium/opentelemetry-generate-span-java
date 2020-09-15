package io.observatorium.opentelemetry.generate.order;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

import io.opentelemetry.common.AttributeValue;
import io.opentelemetry.common.Attributes;
import io.opentelemetry.exporters.otlp.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.resources.ResourceAttributes;
import io.opentelemetry.sdk.trace.TracerSdkProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.trace.Tracer;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class TracerInitializer {
    TracerSdkProvider sdkProvider;

    @Produces
    @Order
    Tracer tracer;

    void onStart(@Observes StartupEvent ev) {
        sdkProvider = TracerSdkProvider.builder().setResource(Resource.create(
                Attributes.of(ResourceAttributes.SERVICE_NAME.key(), AttributeValue.stringAttributeValue("order"))))
                .build();

        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.getDefault();
        BatchSpanProcessor spanProcessor = BatchSpanProcessor.newBuilder(spanExporter).setScheduleDelayMillis(100)
                .build();
        sdkProvider.addSpanProcessor(spanProcessor);

        tracer = sdkProvider.get("order");
    }

    void onStop(@Observes ShutdownEvent ev) {
        // shutdown for processors and exporters should be called as a result of
        // shutting down the tracing provider
        sdkProvider.shutdown();
    }
}
