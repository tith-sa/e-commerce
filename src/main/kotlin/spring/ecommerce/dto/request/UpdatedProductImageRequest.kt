package spring.ecommerce.dto.request

data class UpdatedProductImageRequest(
    val imageUrl: String?,
    val displayOrder: Int?,
)