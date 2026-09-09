package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.ecommerce.model.Order

interface OrderRepository: JpaRepository<Order, Long> {
}