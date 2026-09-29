package spring.ecommerce.dto.response

import java.time.LocalDateTime


data class PermissionResponse(
    val id: Long?,
    val parentId: Long?,
    val code: String?,
    val name: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)