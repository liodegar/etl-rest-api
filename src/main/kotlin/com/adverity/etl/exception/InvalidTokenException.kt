package com.adverity.etl.exception

import java.lang.RuntimeException

/**
 * Exception to be thrown if the provided token is invalid.
 */
class InvalidTokenException(msg: String) : RuntimeException() {

}
