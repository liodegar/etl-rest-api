package com.adverity.etl.domain

import java.math.BigDecimal

data class SummaryStatistics(val statsPerMetrics: List<StatsPerMetric>, val clickThroughRate: BigDecimal) {
}