package spring.ecommerce.repository

import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.ProductImage

@Repository
interface ProductImageRepository: JpaRepository<ProductImage, Long> {
    fun findAllByProductIdOrderByDisplayOrderAsc(id: Long) : List<ProductImage>
    fun findAllByProductId(productId: Long) : List<ProductImage>
    fun findAllByProductIdAndIsPrimary(id: Long, isPrimary: Boolean): ProductImage
}