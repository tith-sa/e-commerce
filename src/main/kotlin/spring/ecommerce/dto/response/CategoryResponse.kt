package spring.ecommerce.dto.response

import java.time.LocalDateTime

data class CategoryResponse(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)