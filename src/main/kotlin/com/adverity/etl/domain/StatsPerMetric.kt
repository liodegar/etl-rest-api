package com.adverity.etl.domain

import java.math.BigDecimal

class StatsPerMetric(val metric: Metric,
                     val count: Long,
                     val sum: Long,
                     val min: Long,
                     val max: Long,
                     val avg: BigDecimal) {
}