package spring.ecommerce.dto.request

data class SearchCustomerRequest(
    val fullName: String?,
    val phoneNumber: String?,
    val address: String?,
    val isDeleted: Boolean?,
)