package com.adverity.etl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class TaskApplication {

    @Bean
    fun docket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.adverity.etl"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(generateApiInfo())
    }

    private fun generateApiInfo(): ApiInfo? {
        return ApiInfo("Adverity ETL Service", "This service is a ETL task.", "Version 1.0",
                "urn:tos", "liodegar@gmail.com", "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0")
    }
}

fun main(args: Array<String>) {
    runApplication<TaskApplication>(*args)
}


