package spring.ecommerce.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import spring.ecommerce.model.Product

@Repository
interface ProductRepository: JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    fun findAllByOrderByCreatedAtDesc(pageable: PageRequest) : Page<Product>

}