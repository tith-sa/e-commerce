package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.LoginRequest
import spring.ecommerce.dto.request.RefreshAccessTokenRequest
import spring.ecommerce.dto.response.LoginResponse
import spring.ecommerce.dto.response.RefreshAccessTokenResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.RefreshToken
import spring.ecommerce.repository.RefreshTokenRepository
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.security.JwtUtil
import spring.ecommerce.service.`interface`.AuthService
import java.time.LocalDateTime

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val roleRepository: RoleRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) : AuthService {

    override fun login(request: LoginRequest): Response<LoginResponse> {
        val (username, password) = request

        val user = userRepository.findByUsername(username).orElseThrow {
            NotFoundException("User not found")
        }

        if (!passwordEncoder.matches(password, user.password)){
            throw BadRequestException("Incorrect password")
        }

        val roleId = user.roleId
            ?: throw NotFoundException("Role not found")

        val roleName = roleRepository.findById(roleId).orElseThrow{
            throw NotFoundException("Role not found")
        }

        val userId = user.id
            ?: throw BadRequestException("User not found")

        val accessToken = jwtUtil.generateAccessToken(userId, roleName.name!!)
        val refreshToken = jwtUtil.generateRefreshToken(userId)

        // Save refresh token in database
        refreshTokenRepository.save(
            RefreshToken(
                token = refreshToken,
                userId = userId,
                expiresAt = LocalDateTime.now().plusDays(7)
            )
        )

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

        if (tokenType != "refresh_token") {
            throw BadRequestException("Invalid refresh token")
        }

        // 2. Find refresh token in database
        val storedToken = refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow {
                BadRequestException("Invalid refresh token")
            }

        // 3. Check if token was revoked
        if (storedToken.revoked) {
            throw BadRequestException("Refresh token has been revoked")
        }

        // 4. Check database expiration
        if (storedToken.expiresAt.isBefore(LocalDateTime.now())) {
            throw BadRequestException("Refresh token has expired")
        }

        val userId = jwtUtil.extractUserId(refreshToken)
            ?: throw BadRequestException("Invalid refresh token")

        val user = userRepository.findById(userId)
            .orElseThrow {
                NotFoundException("User not found")
            }

        val roleId = user.roleId
            ?: throw NotFoundException("Role not found")

        val role = roleRepository.findById(roleId)
            .orElseThrow {
                NotFoundException("Role not found")
            }

        val accessToken = jwtUtil.generateAccessToken(
            user.id!!,
            role.name!!
        )

        val response = RefreshAccessTokenResponse(
            accessToken = accessToken,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Access token refreshed"
        )
    }

    override fun logout(userId : Long): Response<Unit> {
        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "User logged out"
        )
    }
}