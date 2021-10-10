package com.adverity.etl.controller

import com.adverity.etl.aop.TokenRequired
import com.adverity.etl.domain.Metric
import com.adverity.etl.domain.StatsPerMetric
import com.adverity.etl.domain.SummaryStatistics
import com.adverity.etl.controller.dto.WebTrackingDTO
import com.adverity.etl.service.WebTrackingService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.time.LocalDate


/**
 * Controller to expose all the enabled web tracking APIs.
 */
@RestController
@RequestMapping("v1/adverity")
class ApiController(private val webTrackingService: WebTrackingService) {

    /**
     * API that returns a list of WebTracking objects for the given dataSource and params.
     * Queries by date range requires both startDate and endDate parameters.
     */
    @TokenRequired
    @GetMapping(value = ["/web-trackings/data-sources/{dataSource}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getWebTrackingsForDataSourceAndParams(@PathVariable(value = "dataSource") dataSource: String,
                                              @RequestParam(value = "campaign", required = false) campaign: String?,
                                              @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
                                              @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?): ResponseEntity<List<WebTrackingDTO>> {

        checkDates(startDate, endDate)
        val result = webTrackingService.getWebTrackingsForDataSourceAndParams(dataSource, campaign, startDate, endDate)
        if (result.isEmpty()) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        return ResponseEntity<List<WebTrackingDTO>>(result.toDTO(), HttpStatus.OK)
    }


    /**
     * API that returns statistic aggregated values like count, sum, min, max, avg per metric
     * and for the given dataSource and params.
     * Queries by date range requires both startDate and endDate parameters.
     */
    @TokenRequired
    @GetMapping(value = ["/statistics/metrics/{metric}/data-sources/{dataSource}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getStatisticsPerMetricForDataSourceAndParams(@PathVariable(value = "metric") metric: Metric,
                                                     @PathVariable(value = "dataSource") dataSource: String,
                                                     @RequestParam(value = "campaign", required = false) campaign: String?,
                                                     @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
                                                     @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?): ResponseEntity<StatsPerMetric> {

        checkDates(startDate, endDate)
        val result = webTrackingService.getStatsPerMetricForDataSourceAndParams(metric, dataSource, campaign, startDate, endDate)
        return result?.let {
            ResponseEntity<StatsPerMetric>(result, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }


    /**
     * API that returns a statistic summary for the given dataSource and params.
     * Queries by date range requires both startDate and endDate parameters.
     */
    @TokenRequired
    @GetMapping(value = ["/summary-statistics/data-sources/{dataSource}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSummaryStatisticsForDataSourceAndParams(@PathVariable(value = "dataSource") dataSource: String,
                                                   @RequestParam(value = "campaign", required = false) campaign: String?,
                                                   @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
                                                   @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?): ResponseEntity<SummaryStatistics> {

        checkDates(startDate, endDate)
        val result = webTrackingService.getSummaryStatisticsForDataSourceAndParams(dataSource, campaign, startDate, endDate)
        return result?.let {
            ResponseEntity<SummaryStatistics>(result, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    private fun checkDates(startDate: LocalDate?, endDate: LocalDate?) {
        if (listOfNotNull(startDate, endDate).size == 1) {
            throw IllegalArgumentException("The range date query requires both dates. Please check the range=[$startDate, $endDate]")
        }
    }
}