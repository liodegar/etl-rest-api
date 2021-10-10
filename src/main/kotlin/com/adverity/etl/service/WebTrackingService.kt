package com.adverity.etl.service

import com.adverity.etl.domain.Metric
import com.adverity.etl.domain.StatsPerMetric
import com.adverity.etl.domain.SummaryStatistics
import com.adverity.etl.domain.WebTracking
import java.time.LocalDate

/**
 * Interface that defines the business contract for retrieving web trackings and stats.
 */
interface WebTrackingService {
    /**
     * Gets a list [WebTracking] objects that matches the given parameters.
     */
    fun getWebTrackingsForDataSourceAndParams(dataSource: String,
                                              campaign: String?,
                                              startDate: LocalDate?,
                                              endDate: LocalDate?): List<WebTracking>

    /**
     * Gets a StatsPerMetric object if the given parameters matches any result, null otherwise.
     */
    fun getStatsPerMetricForDataSourceAndParams(metric: Metric,
                                                dataSource: String,
                                                campaign: String?,
                                                startDate: LocalDate?,
                                                endDate: LocalDate?): StatsPerMetric?

    /**
     * Gets a SummaryStatistics object if the given parameters matches any result, null otherwise.
     */
    fun getSummaryStatisticsForDataSourceAndParams(dataSource: String,
                                                   campaign: String?,
                                                   startDate: LocalDate?,
                                                   endDate: LocalDate?): SummaryStatistics?

}
