package spring.ecommerce.dto.response

data class UserResponse(
    val id: Long? = null,
    val username: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val isDeleted: Boolean,
)