package com.adverity.etl.aop

/**
 * Annotation to activate the security of the controller endpoints
 * Created by Lio.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenRequired