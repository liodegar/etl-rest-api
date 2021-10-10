package com.adverity.etl.controller.dto

import java.time.LocalDate

data class WebTrackingDTO(val dataSource: String,
                          val campaign: String,
                          val daily: LocalDate,
                          val clicks: Int,
                          val impressions: Int) {

}
