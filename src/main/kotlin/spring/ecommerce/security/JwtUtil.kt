package spring.ecommerce.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import spring.ecommerce.model.enum.TokenType
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey


@Component
class JwtUtil(

    @param:Value($$"${jwt.access.secret}")
    private val accessSecretKey: String,

    @param:Value($$"${jwt.refresh.secret}")
    private val refreshSecretKey: String,

    @param:Value($$"${jwt.access.expiration}")
    private val expirationAccessToken: Int,

    @param:Value($$"${jwt.refresh.expiration}")
    private val expirationRefreshToken: Int
) {
    val accessSecret: SecretKey = Keys.hmacShaKeyFor(accessSecretKey.toByteArray())
    val refreshSecret: SecretKey = Keys.hmacShaKeyFor(refreshSecretKey.toByteArray())

    fun generateAccessToken(userId: Long): String {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("type", TokenType.ACCESS_TOKEN.value)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationAccessToken * 1000))
            .signWith(accessSecret, Jwts.SIG.HS256)
            .compact()
    }


    fun generateRefreshToken(userId: Long): String {
        return Jwts.builder()
            .subject(userId.toString())
            .claim("type", TokenType.REFRESH_TOKEN.value)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationRefreshToken * 1000))
            .signWith(refreshSecret, Jwts.SIG.HS256)
            .compact()
    }

    private fun extractAccessClaims(token: String) =
        Jwts.parser()
            .verifyWith(accessSecret)
            .build()
            .parseSignedClaims(token)
            .payload

    private fun extractRefreshClaims(token: String) =
        Jwts.parser()
            .verifyWith(refreshSecret)
            .build()
            .parseSignedClaims(token)
            .payload



    fun extractAccessUserId(token: String): Long? = extractAccessClaims(token).subject.toLong()
    fun extractTokenType(token: String): Int? = extractRefreshClaims(token)["type"].toString().toInt()
    fun extractAccessExpiration(token: String): Date = extractAccessClaims(token).expiration
    fun extractRefreshExpiration(token: String): Date = extractRefreshClaims(token).expiration
}