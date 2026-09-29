package spring.ecommerce.dto.request

data class SearchUserRequest(
    val search: String?,
    val username: String?,
    val phoneNumber: String?,
    val address: String?,
)
