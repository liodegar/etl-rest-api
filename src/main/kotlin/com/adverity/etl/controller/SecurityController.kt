package com.adverity.etl.controller

import com.adverity.etl.service.SecurityService
import com.adverity.etl.util.SIX_MINUTES
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * This controller represents the entry point for the security endpoints
 * Created by Lio.
 */
@RestController
@RequestMapping("v1/security")
class SecurityController(private val securityService: SecurityService) {

    /**
     * Generates the token for the given user.
     *
     * @param subject the JWT subject
     * @return a generated token valid for 6 minutes.
     */
    @ApiOperation(value = "Generates the JWT", httpMethod = "GET")
    @GetMapping("/generate-token")
    fun generateToken(@RequestParam subject: String?): String? {
        return securityService.generateToken(subject, SIX_MINUTES)
    }
}