package com.adverity.etl.domain

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "web_tracking", uniqueConstraints = [
    UniqueConstraint(name = "unique_keys", columnNames = ["datasource", "campaign", "daily"])
])
class WebTracking(@Id
                  @GeneratedValue(strategy = GenerationType.IDENTITY)
                  var id: Long = 0,

                  @Column(nullable = false, name = "datasource", length = 50)
                  val dataSource: String,

                  @Column(nullable = false, length = 50)
                  val campaign: String,

                  @Column(nullable = false)
                  val daily: LocalDate,

                  @Column(nullable = false)
                  val clicks: Int,

                  @Column(nullable = false)
                  val impressions: Int) {
}