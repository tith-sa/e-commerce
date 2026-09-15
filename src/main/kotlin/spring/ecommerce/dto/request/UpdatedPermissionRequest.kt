package spring.ecommerce.dto.request

data class UpdatedPermissionRequest(
    val description: String?,
    val roles: List<String>?
)