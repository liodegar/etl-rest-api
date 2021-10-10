package com.adverity.etl.controller

import com.adverity.etl.service.WebTrackingService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class ApiControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webTrackingService: WebTrackingService

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = ["USER"])
    fun testGetWebTrackingsForDataSourceAndParamsForInvalidDataSource() {
        mockMvc.get("/v1/adverity/web-trackings/data-sources/unknownDataSource") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { status().isNotFound }
        }
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = ["USER"])
    fun getWebTrackingsForDataSourceAndParamsForValidParameters() {
        //Generate token
        val token: String = getAuthorizationToken()

        mockMvc.get("/v1/adverity/web-trackings/data-sources/Twitter Ads?campaign=AT|SN|Snow Helme|Brands") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header("authorizationToken", token)
        }.andExpect {
            status { status().isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json("""
                    [
                      {
                        "dataSource": "Twitter Ads",
                        "campaign": "AT|SN|Snow Helme|Brands",
                        "daily": "2019-01-27",
                        "clicks": 2,
                        "impressions": 11
                      },
                      {
                        "dataSource": "Twitter Ads",
                        "campaign": "AT|SN|Snow Helme|Brands",
                        "daily": "2019-01-28",
                        "clicks": 0,
                        "impressions": 1
                      },
                      {
                        "dataSource": "Twitter Ads",
                        "campaign": "AT|SN|Snow Helme|Brands",
                        "daily": "2019-01-29",
                        "clicks": 0,
                        "impressions": 5
                      },
                      {
                        "dataSource": "Twitter Ads",
                        "campaign": "AT|SN|Snow Helme|Brands",
                        "daily": "2019-01-30",
                        "clicks": 0,
                        "impressions": 1
                      },
                      {
                        "dataSource": "Twitter Ads",
                        "campaign": "AT|SN|Snow Helme|Brands",
                        "daily": "2019-01-31",
                        "clicks": 0,
                        "impressions": 1
                      },
                      {
                        "dataSource": "Twitter Ads",
                        "campaign": "AT|SN|Snow Helme|Brands",
                        "daily": "2019-02-01",
                        "clicks": 0,
                        "impressions": 6
                      }
                    ]
                    """
                        .trimMargin())
            }
        }

    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = ["USER"])
    fun getStatisticsPerMetricForDataSourceAndParamsForInvalidDataSource() {
        mockMvc.get("/v1/adverity/statistics/metrics/CLICKS/data-sources/unknownDataSource") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { status().isNotFound }
        }
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = ["USER"])
    fun getStatisticsPerMetricForDataSourceAndParamsForValidParameters() {
        //Generate token
        val token: String = getAuthorizationToken()
        mockMvc.get("/v1/adverity/statistics/metrics/CLICKS/data-sources/Twitter Ads") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header("authorizationToken", token)
        }.andExpect {
            status { status().isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json("""
                    {
                      "metric": "CLICKS",
                      "count": 6,
                      "sum": 2,
                      "min": 0,
                      "max": 2,
                      "avg": 0.33
                    }
                    """
                        .trimMargin())
            }
        }

    }


    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = ["USER"])
    fun getSummaryStatisticsForDataSourceAndParamsForInvalidDataSource() {
        mockMvc.get("/v1/adverity/summary-statistics/data-sources/unknownDataSource") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { status().isNotFound }
        }
    }


    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = ["USER"])
    fun getSummaryStatisticsForDataSourceAndParamsForValidParameters() {
        //Generate token
        val token: String = getAuthorizationToken()

        mockMvc.get("/v1/adverity/summary-statistics/data-sources/Facebook Ads") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header("authorizationToken", token)
        }.andExpect {
            status { status().isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json("""
                   {
                      "statsPerMetrics": [
                        {
                          "metric": "CLICKS",
                          "count": 5,
                          "sum": 21,
                          "min": 0,
                          "max": 10,
                          "avg": 4.2
                        },
                        {
                          "metric": "IMPRESSIONS",
                          "count": 5,
                          "sum": 138,
                          "min": 15,
                          "max": 37,
                          "avg": 27.6
                        }
                      ],
                      "clickThroughRate": 0.15
                   }
                    """
                        .trimMargin())
            }
        }
    }

    private fun getAuthorizationToken(): String {
        val response = mockMvc.get("/v1/security/generate-token?subject=appSubject") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andReturn().response

        return response.contentAsString
    }
}