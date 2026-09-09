package spring.ecommerce.dto.response

import spring.ecommerce.model.enum.OrderStatus
import spring.ecommerce.model.enum.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long?,
    val customerId: Long?,
    val customerFullName: String?,
    val customerPhoneNumber: String?,
    val customerAddress: String?,
    val totalAmount: BigDecimal?,
    val orderItems: List<OrderItemResponse>? = emptyList(),
    val orderStatus: OrderStatus?,
    val paymentStatus: PaymentStatus?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    )