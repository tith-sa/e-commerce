package spring.ecommerce.dto.request

import jakarta.validation.constraints.NotBlank


data class RoleRequest(

    @field:NotBlank(message = "Name is required")
    val name: String,

    val description: String? = null
)
