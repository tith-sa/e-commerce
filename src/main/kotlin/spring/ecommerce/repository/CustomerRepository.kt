package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import spring.ecommerce.model.Customer
import java.util.Optional

interface CustomerRepository: JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByIdAndIsDeletedFalse(customerId: Long) : Optional<Customer>
}