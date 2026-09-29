package spring.ecommerce.service.`interface`

import jakarta.servlet.http.HttpServletRequest
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.LoginRequest
import spring.ecommerce.dto.request.RefreshAccessTokenRequest
import spring.ecommerce.dto.response.LoginResponse
import spring.ecommerce.dto.response.RefreshAccessTokenResponse

interface AuthService {
    fun login(request: LoginRequest): Response<LoginResponse>
    fun logout(request: RefreshAccessTokenRequest): Response<Unit>
    fun refreshAccessToken(request: RefreshAccessTokenRequest): Response<RefreshAccessTokenResponse>
}