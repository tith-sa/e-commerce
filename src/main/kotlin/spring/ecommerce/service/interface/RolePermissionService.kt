package spring.ecommerce.service.`interface`

import spring.ecommerce.model.RolePermission

interface RolePermissionService {
    fun assignPermission( permissionId : Long, roleId : Long ): RolePermission
    fun hasPermission(roleId: Long, permissionName: String): Boolean
//    fun deleteByPermissionId(permissionId: Long)
}