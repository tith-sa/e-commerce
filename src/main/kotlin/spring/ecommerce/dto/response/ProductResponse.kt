package spring.ecommerce.dto.response

import java.math.BigDecimal


data class ProductResponse(
    val id: Long? = null,
    val name: String? = null,
    val price: BigDecimal? = null,
    val description: String? = null,
    val createdBy: String? = null,
    val categoryName: String? = null,
    val images: List<ProductImageResponse>? = emptyList(),
)