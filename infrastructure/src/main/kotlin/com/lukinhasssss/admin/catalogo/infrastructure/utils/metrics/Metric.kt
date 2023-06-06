package com.lukinhasssss.admin.catalogo.infrastructure.utils.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@Component
class Metric(private val meterRegistry: MeterRegistry) {

    fun <T> measureExecutionTime(processName: String, block: () -> T): T {
        val timer: Timer = meterRegistry.timer("metric_name")

        val result: T

        val duration = measureTimeMillis {
            result = block()
        }

        timer.record(duration, TimeUnit.MILLISECONDS)

        println(processName)

        return result
    }
}
