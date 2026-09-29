package spring.ecommerce.dto.response

import java.time.LocalDateTime

data class RoleResponse(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val permissions: List<PermissionResponse>? = emptyList(),
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)
