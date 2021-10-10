package com.adverity.etl.controller

import com.adverity.etl.domain.WebTracking
import com.adverity.etl.controller.dto.WebTrackingDTO


fun Collection<WebTracking>.toDTO(): List<WebTrackingDTO> {
    return this.map { it.toDTO() }
}

fun WebTracking.toDTO() = WebTrackingDTO(dataSource = this.dataSource,
        campaign = this.campaign,
        daily = this.daily,
        clicks = this.clicks,
        impressions = this.impressions)

