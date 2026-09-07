package spring.ecommerce.seeding

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.User
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.repository.UserRepository

@Component
class AdminSeeder(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,

    @param:Value($$"${admin.username}")
    private val username: String,

    @param:Value($$"${admin.password}")
    private val password: String,

    @param:Value($$"${admin.phoneNumber}")
    private val adminPhoneNumber: String
) {

    private val logger = LoggerFactory.getLogger(AdminSeeder::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun seedAdminOnStartup() {

        // Check whether admin already exists
        val adminUsername = username.lowercase()

        if (
            userRepository.existsByUsername(adminUsername) ||
            userRepository.existsByPhoneNumber(adminPhoneNumber)
        ) {
            logger.info("Default admin already exists. Skipping admin seeding.")
            return
        }

        val hashPassword = passwordEncoder.encode(password)

        // Find ADMIN role
        val role = roleRepository.findByNameIgnoreCase("ADMIN")
            .orElseThrow {
                NotFoundException("Role not found.")
            }

        // Create default admin
        val admin = User().apply {
            username = adminUsername
            phoneNumber = adminPhoneNumber
            password = hashPassword
            roleId = role.id
        }

        userRepository.save(admin)

        logger.info("Default admin user successfully seeded.")
    }
}