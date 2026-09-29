package spring.ecommerce.dto.request

data class UpdatedPermissionRequest(
    val parentId : Long?,
    val name : String?,
    val roles: List<Long>?,
)