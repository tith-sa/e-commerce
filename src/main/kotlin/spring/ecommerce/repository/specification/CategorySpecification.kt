package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.ecommerce.model.Category

fun categorySpecification(
    name: String?,
): Specification<Category> {

    return Specification
        .where(Specifications.like<Category>("name",name))
}