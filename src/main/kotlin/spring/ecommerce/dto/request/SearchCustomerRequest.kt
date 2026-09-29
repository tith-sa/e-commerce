package spring.ecommerce.dto.request

data class SearchCustomerRequest(
    val search: String?,
    val fullName: String?,
    val phoneNumber: String?,
    val address: String?,
)