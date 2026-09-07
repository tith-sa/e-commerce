package spring.ecommerce.dto.response

data class CustomerResponse(
    val id: Long? = null,
    val fullName: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val isDeleted: Boolean? = null,
)