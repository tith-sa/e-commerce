package spring.ecommerce.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import spring.ecommerce.model.Category
import java.util.Optional

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun existsByName(name: String): Boolean
    fun findByNameContaining(name: String) : List<Category>
    fun findByName(name: String) : Optional<Category>
}