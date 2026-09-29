package spring.ecommerce.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import spring.ecommerce.handleException.UnauthorizationException
import spring.ecommerce.repository.ProvideTokenRepository

@Component
class JwtInterceptor(
    private val jwtUtil: JwtUtil,
    private val provideTokenRepository: ProvideTokenRepository
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ) : Boolean{
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw UnauthorizationException("Unauthorized")
        }

        val  token = authHeader.removePrefix("Bearer ")

        val blackListToken = provideTokenRepository.findByAccessToken(token).orElseThrow {
            UnauthorizationException("Unauthorized")
        }

        if (blackListToken.revoked) {
            throw UnauthorizationException("Unauthorized")
        }

        try {
            val userId = jwtUtil.extractAccessUserId(token)

            request.setAttribute("userId", userId)

        } catch (e: Exception) {
            throw UnauthorizationException("Unauthorized")
        }
        return true
    }
}