package spring.ecommerce.dto.request

import java.math.BigDecimal


data class SearchProductRequest(
    val search: String?,
    val name: String?,
    val createById: Long?,
    val categoryId: Long?,
    val minPrice: BigDecimal?,
    val maxPrice: BigDecimal?,
)
