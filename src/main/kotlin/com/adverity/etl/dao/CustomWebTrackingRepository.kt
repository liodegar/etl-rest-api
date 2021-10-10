package com.adverity.etl.dao

import com.adverity.etl.domain.Metric
import com.adverity.etl.domain.StatsPerMetric
import com.adverity.etl.domain.WebTracking
import java.time.LocalDate

interface CustomWebTrackingRepository {

    fun getWebTrackingsForDataSourceAndParams(dataSource: String,
                                              campaign: String?,
                                              startDate: LocalDate?,
                                              endDate: LocalDate?): List<WebTracking>

    fun getStatsPerMetricForDataSourceAndParams(column: Metric,
                                                dataSource: String,
                                                campaign: String?,
                                                startDate: LocalDate?,
                                                endDate: LocalDate?): StatsPerMetric?

}