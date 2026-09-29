package spring.ecommerce.dto.response

import java.time.LocalDateTime

data class CustomerResponse(
    val id: Long? = null,
    val fullName: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)