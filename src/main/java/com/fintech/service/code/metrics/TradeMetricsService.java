package com.fintech.service.code.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TradeMetricsService {

    private final MeterRegistry meterRegistry;
    private final Timer tradeLatencyTimer;

    public TradeMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.tradeLatencyTimer = Timer.builder("order.match.latency")
                .description("Order matching latency")
                .register(meterRegistry);
    }

    public void recordTradeLatency(long durationMs) {
        tradeLatencyTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public void incrementTradeCount() {
        meterRegistry.counter("trades.executed.count").increment();
    }


}
