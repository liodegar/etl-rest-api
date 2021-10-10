package com.adverity.etl.dao

import com.adverity.etl.domain.Metric
import com.adverity.etl.domain.StatsPerMetric
import com.adverity.etl.domain.WebTracking
import com.adverity.etl.util.whenAllNotNull
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query


@Repository
class CustomWebTrackingRepositoryImpl(
        @PersistenceContext private val entityManager: EntityManager) : CustomWebTrackingRepository {

    override fun getWebTrackingsForDataSourceAndParams(dataSource: String,
                                                       campaign: String?,
                                                       startDate: LocalDate?,
                                                       endDate: LocalDate?): List<WebTracking> {

        val sb = StringBuilder(300)
        sb.append("SELECT id, datasource, campaign, daily, clicks, impressions ")
        sb.append("FROM WEB_TRACKING ")
        sb.append("WHERE datasource = :dataSource ")

        listOf(startDate, endDate).whenAllNotNull {
            sb.append("AND daily BETWEEN :startDate AND :endDate ")
        }

        campaign?.let {
            sb.append("AND campaign = :campaign ")
            sb.append("ORDER BY datasource, campaign")
        } ?: run {
            sb.append("ORDER BY datasource")
        }

        val query = entityManager.createNativeQuery(sb.toString(), WebTracking::class.java)
        bindQueryParameters(query, dataSource, campaign, startDate, endDate)

        return query.resultList as List<WebTracking>
    }

    override fun getStatsPerMetricForDataSourceAndParams(column: Metric,
                                                         dataSource: String,
                                                         campaign: String?,
                                                         startDate: LocalDate?,
                                                         endDate: LocalDate?): StatsPerMetric? {

        val sb = StringBuilder(300)
        sb.append("SELECT COUNT(id) AS count,  SUM(${column.name}) AS SUM, MIN(${column.name}) AS min, MAX(${column.name}) AS max, AVG(CAST(${column.name} AS Double)) AS avg ")
        sb.append("FROM WEB_TRACKING ")
        sb.append("WHERE datasource = :dataSource ")

        listOf(startDate, endDate).whenAllNotNull {
            sb.append("AND daily BETWEEN :startDate AND :endDate ")
        }

        campaign?.let {
            sb.append("AND campaign = :campaign ")
            sb.append("GROUP BY datasource, campaign")
        } ?: run {
            sb.append("GROUP BY datasource")
        }

        val query = entityManager.createNativeQuery(sb.toString())
        bindQueryParameters(query, dataSource, campaign, startDate, endDate)

        return toStatsPerMetric(column, query.singleResult)
    }

    private fun toStatsPerMetric(metric: Metric, singleResult: Any?): StatsPerMetric? {
        val resultSet: Array<Any>? = singleResult as? Array<Any>?
        val target: Array<Number>? = resultSet?.toList()
                ?.map { i -> i as Number }
                ?.toTypedArray() ?: null

        return target?.let {
            StatsPerMetric(metric = metric,
                    count = target[0].toLong(),
                    sum = target[1].toLong(),
                    min = target[2].toLong(),
                    max = target[3].toLong(),
                    avg = BigDecimal( target[4].toString()).setScale(2, RoundingMode.HALF_EVEN))
        } ?: null
    }

    private fun bindQueryParameters(query: Query, dataSource: String, campaign: String?, startDate: LocalDate?,
                                    endDate: LocalDate?) {
        query.setParameter("dataSource", dataSource)
        campaign?.let {
            query.setParameter("campaign", campaign)
        }
        listOf(startDate, endDate).whenAllNotNull {
            query.setParameter("startDate", startDate)
            query.setParameter("endDate", endDate)
        }
    }

}