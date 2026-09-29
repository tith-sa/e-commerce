package spring.ecommerce.dto.request

import org.jetbrains.annotations.NotNull

data class UpdatedOrderStatusRequest(

    @field:NotNull("Status is request")
    val status: Int
)