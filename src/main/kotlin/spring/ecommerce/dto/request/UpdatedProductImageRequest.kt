package spring.ecommerce.dto.request

data class UpdatedProductImageRequest(
    val imageUrl: String?,
    val isPrimary: Boolean?,
    val displayOrder: Int?,
)