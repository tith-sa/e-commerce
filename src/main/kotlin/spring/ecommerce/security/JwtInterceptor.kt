package spring.ecommerce.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import spring.ecommerce.handleException.UnauthorizationException

@Component
class JwtInterceptor(
    private val jwtUtil: JwtUtil,
): HandlerInterceptor {

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

        try {
            val userId = jwtUtil.extractUserId(token)
            val roleName = jwtUtil.extractRoleId(token)

            request.setAttribute("userId", userId)
            request.setAttribute("roleName", roleName)

        } catch (e: UnauthorizationException) {
            throw UnauthorizationException("Unauthorized")
        }
        return true
    }
}