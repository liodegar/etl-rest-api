package com.adverity.etl.controller

import com.adverity.etl.exception.AuthorizationException
import com.adverity.etl.exception.BusinessException
import com.adverity.etl.exception.InvalidTokenException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

private val logger = KotlinLogging.logger {}

/**
 * Class for properly handling any uncaught exception.
 */
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        logger.error(e) {}
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = [BusinessException::class])
    fun handleBusinessException(e: BusinessException): ResponseEntity<String> {
        logger.error(e) {}
        return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = [AuthorizationException::class])
    fun handleAuthorizationException(e: AuthorizationException): ResponseEntity<String> {
        logger.error(e) {}
        return ResponseEntity(e.message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = [InvalidTokenException::class])
    fun handleInvalidTokenException(e: InvalidTokenException): ResponseEntity<String> {
        logger.error(e) {}
        return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED);
    }
}