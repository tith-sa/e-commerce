package spring.ecommerce.dto.response


import java.math.BigDecimal
import java.time.LocalDateTime


data class ProductResponse(
    val id: Long? = null,
    val name: String? = null,
    val price: BigDecimal? = null,
    val description: String? = null,
    val quantity: Int? = null,
    val createdBy: UserResponse? = null,
    val category: CategoryResponse? = null,
    val images: List<ProductImageResponse>? = emptyList(),
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)