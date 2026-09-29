package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.ecommerce.dto.request.SearchUserRequest
import spring.ecommerce.model.User

fun userSpecification(
    search: SearchUserRequest
): Specification<User> {
    val(search, username, phoneNumber, address) = search

    var specification = Specifications.equal<User>("isDeleted", false)

    if (!search.isNullOrBlank()) {
        specification = specification.and(
            Specifications.like<User>("username", search)
                .or(Specifications.like<User>("phoneNumber", search))
                .or(Specifications.like<User>("address", search))
        )
    }

    // Specific username
    if (!username.isNullOrBlank()) {
        specification = specification.and(
            Specifications.like<User>("username", username)
        )
    }

    // Specific phone number
    if (!phoneNumber.isNullOrBlank()) {
        specification = specification.and(
            Specifications.like<User>("phoneNumber", phoneNumber)
        )
    }

    // Specific address
    if (!address.isNullOrBlank()) {
        specification = specification.and(
            Specifications.like<User>("address", address)
        )
    }

//    if (search.isNullOrBlank()) {
//        specification = specification
//            .and(Specifications.like<User>("username", username))
//            .and (Specifications.like<User>("phoneNumber", phoneNumber))
//            .and(Specifications.like<User>("address", address))
//    }

    return specification
}