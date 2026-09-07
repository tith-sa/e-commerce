package spring.ecommerce.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdatedUserRoleRequest(
    @field:NotBlank(message = "Role name is required")
    val roleName: String,
)