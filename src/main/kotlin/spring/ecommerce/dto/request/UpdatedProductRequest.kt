package spring.ecommerce.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class UpdatedProductRequest(
    @field:NotBlank
    val name: String?,

    @field:Positive(message = "Price must be positive")
    val price: BigDecimal?,

    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int?,

    val description: String?,

    @field:NotBlank(message = "Product's category name is required")
    val categoryName: String?,

)