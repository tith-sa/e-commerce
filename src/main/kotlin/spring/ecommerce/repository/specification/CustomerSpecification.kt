package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.ecommerce.dto.request.SearchCustomerRequest
import spring.ecommerce.model.Customer

fun customerSpecification(request: SearchCustomerRequest): Specification<Customer> {
    val (search, fullName, phoneNumber, address) = request

    var specification = Specifications.equal<Customer>("isDeleted", false)

    if (!search.isNullOrBlank()) {
        specification = specification
            .and(Specifications.like<Customer>("fullName", search))
            .or(Specifications.like<Customer>("phoneNumber", search))
            .or(Specifications.like<Customer>("address", search))
    }

    if (!fullName.isNullOrBlank()) {
        specification = specification.and(
            Specifications.like<Customer>("fullName", fullName)
        )
    }

    if(!phoneNumber.isNullOrBlank()) {
        specification = specification.and(
            Specifications.like<Customer>("phoneNumber", phoneNumber)
        )
    }

    if (!address.isNullOrBlank()) {
        specification = specification.and(
            Specifications.like<Customer>("address", address)
        )
    }

    return specification
}