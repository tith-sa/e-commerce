package spring.ecommerce.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class OrderItemRequest(

    @field:NotNull(message = "ProductId is required")
    val productId: Long,

    @field:Min(value = 1, message = "Please input quantity of product")
    val quantity: Int,
)