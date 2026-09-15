package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.Permission
import java.util.Optional

@Repository
interface PermissionRepository : JpaRepository<Permission, Long> {
    fun findByPermissionName(permissionName: String) : Optional<Permission>
    fun existsByPermissionName(permissionName: String): Boolean
}