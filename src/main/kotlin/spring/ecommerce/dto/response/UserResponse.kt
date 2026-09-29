package spring.ecommerce.dto.response

import java.time.LocalDateTime

data class UserResponse(
    val id: Long? = null,
    val username: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)