package spring.ecommerce.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PaginationRequest(

    @field:Min(1, message = "Page must be greater than 0")
    val page: Int = 1,

    @field:Min(1, message = "Size must be greater than 1")
    @field:Max(100, message = "Size must be less than 100")
    val size: Int = 10
)
