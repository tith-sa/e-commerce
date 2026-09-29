package spring.ecommerce.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class PermissionRequest(
    val parentId: Long? = null,

    @field:NotBlank("Code is required")
    val code: String,

    @field:NotNull("Sort order permission is required")
    val sortOrder: Int,

    @field:NotBlank("permission name is required")
    val name : String,

    val roles : List<Long>
)