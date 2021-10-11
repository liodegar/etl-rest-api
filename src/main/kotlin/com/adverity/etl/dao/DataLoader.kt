package com.adverity.etl.dao

import com.adverity.etl.domain.WebTracking
import com.adverity.etl.util.*
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

/**
 * Class in charge of loading the data into the DB from the CSV file.
 */
@Component
class DataLoader(private val webTrackingRepository: WebTrackingRepository) {

    @Value("\${etl.data-loading.resource-path}")
    private lateinit var resourcePath: String

    fun load() {
        try {
            val csvContent = StreamUtils.copyToString(javaClass.classLoader.getResource(resourcePath).openStream(), StandardCharsets.UTF_8)
            val lines = csvContent.split(Regex("\\r?\\n|\\r"))
            lines.drop(1) //discarding header
                    .filterNot { it.isNullOrEmpty() }
                    .map { toWebTracking(it) }
                    .forEach { webTrackingRepository.save(it) }

        } catch (e: Exception) {
            logger.error(e) { "Exception thrown while loading initial data resource=$resourcePath" }
            throw e
        }
    }

    private fun toWebTracking(line: String): WebTracking {
        val tokens = line.split(",")
        return WebTracking(dataSource = tokens[DATA_SOURCE_POSITION],
                campaign = tokens[CAMPAIGN_POSITION],
                daily = LocalDate.parse(tokens[DAILY_POSITION], DateTimeFormatter.ofPattern(CSV_DATE_FORMAT)),
                clicks = tokens[CLICKS_POSITION].toInt(),
                impressions = tokens[IMPRESSIONS_POSITION].toInt())
    }

}
