package com.adverity.etl.aop

import com.adverity.etl.exception.AuthorizationException
import com.adverity.etl.exception.InvalidTokenException
import io.jsonwebtoken.Jwts
import mu.KotlinLogging
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.xml.bind.DatatypeConverter

private val logger = KotlinLogging.logger {}

/**
 * Class in charge of the JWT implementation
 * Created by Lio.
 */
@Aspect
@Component
class TokenRequiredAspect {

    @Value("\${security.tokenName}")
    private lateinit var tokenName: String

    @Value("\${security.secretKeys}")
    private lateinit var secretKeys: String

    @Value("\${security.subject}")
    private lateinit var subject: String

    @Before("@annotation(tokenRequired)")
    fun tokenRequiredWithAnnotation(tokenRequired: TokenRequired?) {
        logger.info("Before tokenRequiredWithAnnotation")
        val reqAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val request = reqAttributes.request
        // checks for token in request header
        val tokenInHeader = request.getHeader(tokenName)
        if (tokenInHeader.isNullOrEmpty()) {
            throw AuthorizationException(msg = "The required token is missing")
        }
        val claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKeys))
                .parseClaimsJws(tokenInHeader).body
        if (claims == null || claims.subject == null) {
            throw AuthorizationException(msg = "Token Error: The Claim or the subject is null")
        }
        if (!claims.subject.equals(subject, ignoreCase = true)) {
            throw InvalidTokenException("Subject doesn't match in the token")
        }
        logger.info("After tokenRequiredWithAnnotation")
    }
}