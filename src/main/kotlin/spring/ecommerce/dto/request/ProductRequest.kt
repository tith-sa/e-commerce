package spring.ecommerce.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class ProductRequest(
    @field:NotBlank
    val name: String,

    @field:Positive(message = "Price must be positive")
    val price: BigDecimal = BigDecimal.ZERO.setScale(2),

    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int,

    val description: String? = null,

    @field:NotNull(message = "Product's category is required")
    val categoryId: Long,

    val images : List<ProductImageRequest> = emptyList(),
)

