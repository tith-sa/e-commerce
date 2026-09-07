package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.LoginRequest
import spring.ecommerce.dto.response.LoginResponse
import spring.ecommerce.service.`interface`.AuthService


@RestController
@RequestMapping("api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    @Operation(summary = "Login")
    fun login(
        @Valid
        @RequestBody request: LoginRequest
    ): ResponseEntity<Response<LoginResponse>> {
        val result = authService.login(request)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    fun logout(
        @RequestAttribute userId: Long
    ): ResponseEntity<Response<String>> {
        val result = authService.logout(userId)
        return ResponseEntity.ok(result)
    }
}