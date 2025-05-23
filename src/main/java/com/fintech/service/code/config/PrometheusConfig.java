package com.fintech.service.code.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class PrometheusConfig {

    private final MeterRegistry meterRegistry;

    public PrometheusConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        meterRegistry.config().commonTags("application", "order-matching-engine");
    }


}
