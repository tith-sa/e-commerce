package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.Permission
import java.util.Optional

@Repository
interface PermissionRepository : JpaRepository<Permission, Long> {
    fun existsByCode(code: String): Boolean
    fun findByParentId(parentId: Long): Optional<Permission>
    fun findByIdIn(ids: Set<Long?>) : Set<Permission>
    fun findByCodeIn(code: Set<String>): Set<Permission>
}