package spring.ecommerce.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdatedUserRequest(
    val username: String?,

    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String?,

    @field:Size(
        min = 4,
        max = 20,
        message = "Password must be between 4 and 20 characters"
    )
    val password: String?,

    val address: String?,
)
