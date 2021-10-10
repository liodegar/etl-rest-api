package com.adverity.etl.service

import com.adverity.etl.exception.AuthorizationException

/**
 * Defines the contract of the security functionalities
 * Created by Lio.
 */
interface SecurityService {

    /**
     * Generates a token if the user is valid
     *
     * @param subject The subject requires
     * @param ttlMillis expiration time in millis
     * @return a token if the user is valid, throws an exception otherwise
     * @throws AuthorizationException if any exception is found, with a message containing contextual information
     * of the error and the root exception
     */
    @Throws(AuthorizationException::class)
    fun generateToken(subject: String?, ttlMillis: Long): String?
}
