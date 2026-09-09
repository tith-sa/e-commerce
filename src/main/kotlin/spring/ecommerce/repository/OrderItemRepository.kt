package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.ecommerce.model.OrderItem

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    fun findAllOrderByOrderId(id: Long) : List<OrderItem>
}