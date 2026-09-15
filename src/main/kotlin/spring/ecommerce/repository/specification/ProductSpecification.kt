package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.ecommerce.dto.request.SearchProductRequest
import spring.ecommerce.model.Category
import spring.ecommerce.model.Product
import spring.ecommerce.model.User
import java.math.BigDecimal

fun productSpecification(
    request: SearchProductRequest
): Specification<Product> {

    val (name, createdBy, categoryName, minPrice, maxPrice) = request
    
    return Specification
        .where (Specifications.like<Product>("name", name))
        .and(
            Specifications.likeById<Product, Category>(
                idField = "categoryId",
                relatedEntity = Category::class,
                relatedIdField = "id",
                relatedNameField = "name",
                value = categoryName
            )
        )
        .and (
            Specifications.likeById<Product, User>(
                idField = "createdBy",
                relatedEntity = User::class,
                relatedIdField = "id",
                relatedNameField = "username",
                value = createdBy
            )
        )
        .and(
            Specifications.greaterThan<Product, BigDecimal>(
                "price",
                minPrice
            )
        )
        .and(
            Specifications.lessThan<Product, BigDecimal>(
                "price",
                maxPrice
            )
        )
}