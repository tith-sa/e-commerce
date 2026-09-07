package spring.ecommerce.dto.request

data class SearchUserRequest(
    val username: String?,
    val phoneNumber: String?,
    val address: String?,
    val isDeleted: Boolean?,
)
