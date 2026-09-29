package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.ProvideToken
import java.util.Optional

@Repository
interface ProvideTokenRepository: JpaRepository<ProvideToken, Long> {
    fun findByRefreshToken(refreshToken: String): Optional<ProvideToken>
    fun findByAccessToken(accessToken: String) : Optional<ProvideToken>
}