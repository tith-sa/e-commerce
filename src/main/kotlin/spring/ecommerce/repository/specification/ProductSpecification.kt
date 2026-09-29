package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.ecommerce.dto.request.SearchProductRequest
import spring.ecommerce.model.Product
import java.math.BigDecimal

fun productSpecification(
    request: SearchProductRequest
): Specification<Product> {

    val (search, name, createdById, categoryId, minPrice, maxPrice) = request

    var specification = Specification<Product> { root, query, cb ->

        query.orderBy(
            cb.desc(root.get<String>("createdAt"))
        )

        cb.conjunction()
    }

    // General search
    if (!search.isNullOrBlank()) {

        var searchSpecification =  Specifications.like<Product>("name", search)

        search.toLongOrNull()?.let {
            searchSpecification = searchSpecification.or(
                Specifications.equal<Product>("categoryId", it)
            )
        }

        specification = specification.and(searchSpecification)

    }


    if(!name.isNullOrBlank()) {
        specification = specification
            .and(Specifications.like<Product>("name", name))
    }
    if (categoryId != null) {
        specification = specification
            .and (Specifications.equal<Product>("categoryId", categoryId))
    }
    if(createdById != null) {
        specification = specification
            .and(Specifications.equal<Product>("createdById", createdById))
    }
    if (minPrice != null) {
        specification = specification
            .and(Specifications.greaterThan<Product, BigDecimal>("price", minPrice))
    }
    if (maxPrice != null) {
        specification = specification
            .and(Specifications.lessThan<Product, BigDecimal>("price", maxPrice))
    }
    
    return specification

}

