package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import spring.ecommerce.config.AppConstraints
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.LoginRequest
import spring.ecommerce.dto.request.RefreshAccessTokenRequest
import spring.ecommerce.dto.response.LoginResponse
import spring.ecommerce.dto.response.RefreshAccessTokenResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.ProvideToken
import spring.ecommerce.model.enum.TokenType
import spring.ecommerce.repository.ProvideTokenRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.security.JwtUtil
import spring.ecommerce.service.`interface`.AuthService
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val provideTokenRepository: ProvideTokenRepository,
) : AuthService {

    override fun login(request: LoginRequest): Response<LoginResponse> {
        val (username, password) = request

        val user = userRepository.findByUsername(username).orElseThrow {
            NotFoundException("User not found")
        }

        if (!passwordEncoder.matches(password, user.password)){
            throw BadRequestException("Incorrect password")
        }

        val userId = user.id
            ?: throw BadRequestException("User not found")

        val accessToken = jwtUtil.generateAccessToken(userId)
        val refreshToken = jwtUtil.generateRefreshToken(userId)

        val accessExpiresAt = jwtUtil.extractAccessExpiration(accessToken)
            .toInstant()
            .atZone(ZoneId.of(AppConstraints.LOCAL_TZ))
            .toLocalDateTime()

        val refreshExpiresAt = jwtUtil.extractRefreshExpiration(refreshToken)
            .toInstant()
            .atZone(ZoneId.of(AppConstraints.LOCAL_TZ))
            .toLocalDateTime()

        val token = ProvideToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId,
            accessExpiresAt = accessExpiresAt,
            refreshExpiresAt = refreshExpiresAt
        )

        provideTokenRepository.save(token)

        val response = LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "user logged in"
        )

    }

    override fun refreshAccessToken(
        request: RefreshAccessTokenRequest
    ): Response<RefreshAccessTokenResponse> {

        val (refreshToken) = request

        val tokenType = jwtUtil.extractTokenType(refreshToken)

        if (tokenType != TokenType.REFRESH_TOKEN.value) {
            throw BadRequestException("Invalid refresh token")
        }

        // 2. Find refresh token in database
        val storedToken = provideTokenRepository
            .findByRefreshToken(refreshToken)
            .orElseThrow {
                BadRequestException("Invalid refresh token")
            }

        // 3. Check if token was revoked
        if (storedToken.revoked) {
            throw BadRequestException("Refresh token has been revoked")
        }

        // 4. Check database expiration
        if (storedToken.refreshExpiresAt?.isBefore(LocalDateTime.now()) == true){
            throw BadRequestException("Refresh token has expired")
        }

        val userId = storedToken.userId
        ?: throw BadRequestException("User not found")

        val newAccessToken = jwtUtil.generateAccessToken(
            userId,
        )

        val accessExpiresAt = jwtUtil.extractAccessExpiration(newAccessToken)
            .toInstant()
            .atZone(ZoneId.of(AppConstraints.LOCAL_TZ))
            .toLocalDateTime()


        storedToken.accessToken = newAccessToken
        storedToken.accessExpiresAt = accessExpiresAt

        provideTokenRepository.save(storedToken)

        val response = RefreshAccessTokenResponse(
            accessToken = newAccessToken,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Access token refreshed"
        )
    }

    override fun logout(request: RefreshAccessTokenRequest): Response<Unit> {
        val (refreshToken) = request
        val token = provideTokenRepository.findByRefreshToken(refreshToken).orElseThrow {
            NotFoundException("Refresh token not found")
        }

        token.revoked = true
        provideTokenRepository.save(token)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "User logged out"
        )
    }

}