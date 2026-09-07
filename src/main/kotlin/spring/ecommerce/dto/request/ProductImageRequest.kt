package spring.ecommerce.dto.request

import jakarta.validation.constraints.NotBlank

data class ProductImageRequest (
    @field:NotBlank ("Image_url is required")
    val imageUrl: String,

    val isPrimary: Boolean = false
)