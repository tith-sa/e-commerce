package spring.ecommerce.dto.request

import jakarta.validation.constraints.Min

data class OrderItemRequest(

    @field:Min(value = 1, message = "Please input quantity of product")
    val quantity: Int,
)