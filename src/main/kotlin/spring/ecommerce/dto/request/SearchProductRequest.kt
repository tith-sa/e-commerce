package spring.ecommerce.dto.request

import java.math.BigDecimal


data class SearchProductRequest(
    val name: String?,
    val createBy: String?,
    val categoryName: String?,
    val minPrice: BigDecimal?,
    val maxPrice: BigDecimal?,
)
