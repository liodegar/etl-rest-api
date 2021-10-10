package com.adverity.etl.service

import com.adverity.etl.dao.WebTrackingRepository
import com.adverity.etl.domain.Metric
import com.adverity.etl.domain.StatsPerMetric
import com.adverity.etl.domain.SummaryStatistics
import com.adverity.etl.domain.WebTracking
import com.adverity.etl.exception.BusinessException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

/**
 * Default WebTrackingService implementation.
 */
@Service
class WebTrackingServiceImpl(private val webTrackingRepository: WebTrackingRepository) : WebTrackingService {

    @Transactional(readOnly = true)
    override fun getWebTrackingsForDataSourceAndParams(dataSource: String,
                                                       campaign: String?,
                                                       startDate: LocalDate?,
                                                       endDate: LocalDate?): List<WebTracking> {
        try {
            return webTrackingRepository.getWebTrackingsForDataSourceAndParams(dataSource, campaign, startDate, endDate)
        } catch (e: Exception) {
            val msg = "Exception thrown while getting WebTrackings for dataSource=$dataSource"
            logger.error(e) { msg }
            throw BusinessException(e, msg)
        }
    }

    @Transactional(readOnly = true)
    override fun getStatsPerMetricForDataSourceAndParams(metric: Metric,
                                                         dataSource: String,
                                                         campaign: String?,
                                                         startDate: LocalDate?,
                                                         endDate: LocalDate?): StatsPerMetric? {
        try {
            return webTrackingRepository.getStatsPerMetricForDataSourceAndParams(metric, dataSource, campaign, startDate, endDate)
        } catch (e: Exception) {
            val msg = "Exception thrown while getting StatsPerMetric for metrics=$metric and dataSource=$dataSource"
            logger.error(e) { msg }
            throw BusinessException(e, msg)
        }
    }

    @Transactional(readOnly = true)
    override fun getSummaryStatisticsForDataSourceAndParams(dataSource: String,
                                                            campaign: String?,
                                                            startDate: LocalDate?,
                                                            endDate: LocalDate?): SummaryStatistics? {
        try {
            val map = enumValues<Metric>()
                    .map { metric -> webTrackingRepository.getStatsPerMetricForDataSourceAndParams(metric, dataSource, campaign, startDate, endDate) }
                    .associateBy { it?.metric }

            val notNullMetrics = map.values.filterNotNull()
            if (notNullMetrics.size == Metric.values().size) {
                return SummaryStatistics(notNullMetrics, getClickThroughRate(map))
            }
            return null
        } catch (e: Exception) {
            val msg = "Exception thrown while getting SummaryStatistics for dataSource=$dataSource"
            logger.error(e) { msg }
            throw BusinessException(e, msg)
        }
    }

    private fun getClickThroughRate(map: Map<Metric?, StatsPerMetric?>): BigDecimal {
        val impressions = map[Metric.IMPRESSIONS]!!.sum
        if (impressions == 0L) {
            return BigDecimal.ZERO
        }
        return BigDecimal(map[Metric.CLICKS]!!.sum)
                .divide(BigDecimal(impressions), 2, RoundingMode.HALF_EVEN)

    }

}