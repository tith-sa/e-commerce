package spring.ecommerce.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey


@Component
class JwtUtil(

    @param:Value($$"${jwt.secret}")
    private val secretKey: String,

    @param:Value($$"${jwt.expiration}")
    private val expirationTime: Int
) {
    val secret: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())
    fun generateToken(userId: Long, roleName: String): String {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("roleName", roleName)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(secret)
            .compact()
    }

    private fun extractClaims(token: String) =
        Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .payload

    fun extractUserId(token: String): Long? = extractClaims(token).subject.toLong()
    fun extractRoleId(token: String): String? = extractClaims(token)["roleName"]?.toString()
}