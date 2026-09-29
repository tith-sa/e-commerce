package spring.ecommerce.dto.request

data class OrderRequest (
    val customerId: Long,
    val remark: String?,
    val orderItems: List<OrderItemRequest> = emptyList()
)