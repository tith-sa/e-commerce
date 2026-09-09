package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.ecommerce.dto.request.SearchCustomerRequest
import spring.ecommerce.model.Customer

fun customerSpecification(request: SearchCustomerRequest): Specification<Customer> {
    val (fullName, phoneNumber, address, isDeleted) = request

    return Specification
        .where(Specifications.like<Customer>("fullName", fullName))
        .and(Specifications.like<Customer>("phoneNumber", phoneNumber))
        .and(Specifications.like<Customer>("address", address))
        .and(Specifications.equal<Customer>("isDeleted", isDeleted))
}