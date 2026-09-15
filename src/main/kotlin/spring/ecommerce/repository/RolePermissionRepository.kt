package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.RolePermission

@Repository
interface RolePermissionRepository: JpaRepository<RolePermission, Long> {
    fun existsByRoleIdAndPermissionId(roleId: Long, permissionId: Long): Boolean
    fun deleteAllByPermissionId(permissionId: Long)
    fun findRoleNamesByPermissionId(id: Long): List<RolePermission>
    fun findByPermissionId(permissionId: Long): List<RolePermission>
}