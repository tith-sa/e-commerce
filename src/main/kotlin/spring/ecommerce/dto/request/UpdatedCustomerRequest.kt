package spring.ecommerce.dto.request

data class UpdatedCustomerRequest(
    val fullName: String?,
    val phoneNumber: String?,
    val address: String?,
)
