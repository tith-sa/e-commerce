package spring.ecommerce.dto.response

import java.math.BigDecimal

data class OrderItemResponse(
    val id: Long?,
    val productName: String?,
    val productOwner: String?,
    val productImage: String?,
    val quantity: Int?,
    val unitPrice: BigDecimal?,
    val amount: BigDecimal?,
)