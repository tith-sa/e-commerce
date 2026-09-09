package spring.ecommerce.dto.request


data class SearchProductRequest(
    val name: String?,
    val createBy: String?,
    val categoryName: String?,
)
