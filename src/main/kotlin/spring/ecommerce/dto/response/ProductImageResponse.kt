package spring.ecommerce.dto.response

data class ProductImageResponse(
    val id: Long? = null,
    val imageUrl: String? = null,
    val displayOrder: Int? = null,
    val isPrimary: Boolean,
)
