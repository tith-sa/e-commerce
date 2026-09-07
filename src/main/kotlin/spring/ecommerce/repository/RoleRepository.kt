package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.Role
import java.util.Optional

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByNameIgnoreCase(name: String): Optional<Role>
    fun existsByNameIgnoreCase(name: String): Boolean
}