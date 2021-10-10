package com.adverity.etl.exception

import java.lang.RuntimeException

/**
 * Exception to be thrown if the request is not authorized.
 */
class AuthorizationException(e: Exception? = null, msg: String) : RuntimeException() {

}
