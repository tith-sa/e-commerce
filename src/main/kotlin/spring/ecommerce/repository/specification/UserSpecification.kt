package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.ecommerce.dto.request.SearchUserRequest
import spring.ecommerce.model.User


fun userSpecification(
    request: SearchUserRequest
): Specification<User> {
    val (username, phoneNumber, address, isDeleted) = request

    return Specification
        .where(Specifications.like<User>("username", username))
        .and(Specifications.like<User>("phoneNumber", phoneNumber))
        .and(Specifications.like<User>("address", address))
        .and(Specifications.equal<User>("isDeleted", isDeleted))
}