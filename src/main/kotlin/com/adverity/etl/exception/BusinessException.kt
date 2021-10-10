package com.adverity.etl.exception

import java.lang.RuntimeException

/**
 * Exception to be thrown if any error is raised at the service layer.
 */
class BusinessException(e: Exception, msg: String) : RuntimeException() {

}
