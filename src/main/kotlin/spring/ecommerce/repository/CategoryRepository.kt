package spring.ecommerce.repository

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import spring.ecommerce.model.Category
import java.util.Optional

@Repository
interface CategoryRepository : JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    fun existsByName(name: String): Boolean
    fun findByIdIn(categoryId: List<Long?>) : List<Category>
}