package com.adverity.etl.dao

import com.adverity.etl.domain.WebTracking
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface WebTrackingRepository : CrudRepository<WebTracking, Long>, CustomWebTrackingRepository {

}
