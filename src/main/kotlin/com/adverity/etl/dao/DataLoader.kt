package com.adverity.etl.dao

import com.adverity.etl.domain.WebTracking
import com.adverity.etl.util.*
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Stream

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
            val path: Path = Paths.get(javaClass.classLoader.getResource(resourcePath).toURI())
            val lines: Stream<String> = Files.lines(path)
            lines.use { stream ->
                stream.skip(1) //discarding header
                        .map { toWebTracking(it) }
                        .forEach { webTrackingRepository.save(it) }
            }
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
