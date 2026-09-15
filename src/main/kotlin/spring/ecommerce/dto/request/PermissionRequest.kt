package spring.ecommerce.dto.request

import jakarta.validation.constraints.NotBlank


data class PermissionRequest(

    @field:NotBlank("permission name is required")
    val permissionName : String,
    val description : String? = null,

    @field:NotBlank("role is required")
    val roles : List<String>
)