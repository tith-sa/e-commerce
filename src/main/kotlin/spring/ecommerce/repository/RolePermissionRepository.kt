package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.RolePermission
import java.util.Optional

@Repository
interface RolePermissionRepository: JpaRepository<RolePermission, Long> {
    fun existsByRoleIdAndPermissionId(roleId: Long, permissionId: Long): Boolean
    fun deleteAllByPermissionId(permissionId: Long)
    fun findByRoleIdIn(roleIds: List<Long?> ): List<RolePermission>
    fun findByRoleId(roleId: Long): List<RolePermission>
}