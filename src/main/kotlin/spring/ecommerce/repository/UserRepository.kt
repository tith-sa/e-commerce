package spring.ecommerce.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import spring.ecommerce.model.User
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    fun existsByUsername(name: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByUsername(username: String): Optional<User>
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable) : Page<User>
}