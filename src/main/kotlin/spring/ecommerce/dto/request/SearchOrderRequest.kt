package spring.ecommerce.dto.request

data class SearchOrderRequest(
    val search: String?,
    val customerPhoneNumber: String?,
    val productIds: List<Long>?,
)
