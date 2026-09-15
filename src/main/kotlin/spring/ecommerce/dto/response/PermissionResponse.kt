package spring.ecommerce.dto.response


data class PermissionResponse(
    val id: Long?,
    val permissionName: String?,
    val description: String?,
    val roles: List<String?>
)