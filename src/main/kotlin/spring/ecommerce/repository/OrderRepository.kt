package spring.ecommerce.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

import spring.ecommerce.model.Order

interface OrderRepository: JpaRepository<Order, Long> , JpaSpecificationExecutor<Order> {
    fun findAllByOrderByIdDesc(pageable: PageRequest): Page<Order>
}