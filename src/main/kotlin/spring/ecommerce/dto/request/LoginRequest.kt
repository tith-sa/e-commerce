package spring.ecommerce.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(

    @field:NotBlank(message = "username is required")
    val username: String,

    @field:Size(
        min = 4,
        max = 20,
        message = "Password must be between 4 and 20 characters"
    )
    val password: String
)