package com.adverity.etl.util

import com.adverity.etl.dao.DataLoader
import mu.KotlinLogging
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private val logger = KotlinLogging.logger {}

@Component
class EventListener(private val dataLoader: DataLoader) {

    @ExperimentalTime
    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent() {
        val elapsed: Duration = measureTime {
            logger.info { "Running the data loading process" }
            dataLoader.load()
        }
        logger.info { "Data loading process finished with a duration of $elapsed" }
    }
}