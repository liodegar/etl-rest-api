package com.adverity.etl.service

import com.adverity.etl.exception.AuthorizationException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

@Service
class SecurityServiceImpl : SecurityService {

    @Value("\${security.secretKeys}")
    private lateinit var secretKeys: String

    override fun generateToken(subject: String?, ttlMillis: Long): String? {
        return try {
            require(ttlMillis > 0) { "Expiry time must be greater than Zero :[$ttlMillis] " }
            // The JWT signature algorithm we will be using to sign the token
            val signatureAlgorithm = SignatureAlgorithm.HS256
            val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKeys)
            val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)
            val builder = Jwts.builder()
                    .setSubject(subject)
                    .signWith(signatureAlgorithm, signingKey)
            val nowMillis = System.currentTimeMillis()
            builder.setExpiration(Date(nowMillis + ttlMillis))
            builder.compact()
        } catch (e: Exception) {
            throw AuthorizationException(e, "Exception encountered generating the security token with param=$subject")
        }
    }
}