package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.ecommerce.model.Customer

interface CustomerRepository: JpaRepository<Customer, Long> {
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}