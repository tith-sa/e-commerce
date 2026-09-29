package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.ecommerce.model.OrderItem

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    fun findAllByOrderId(id: Long) : List<OrderItem>
    fun findAllByProductIdOrderByIdDesc(productId: Long): List<OrderItem>
    fun findAllByOrderIdIn(orderId: List<Long?>): List<OrderItem>
}