package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.ProductImage

@Repository
interface ProductImageRepository: JpaRepository<ProductImage, Long> {
    fun findAllByProductIdOrderByDisplayOrderAsc(id: Long) : List<ProductImage>
}