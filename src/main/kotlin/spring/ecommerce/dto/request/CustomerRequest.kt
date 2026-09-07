package spring.ecommerce.dto.request

import jakarta.validation.constraints.NotBlank

data class CustomerRequest(
    val fullName: String? = null,

    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String,

    val address: String? = null,

    val isDeleted: Boolean = false,
)
