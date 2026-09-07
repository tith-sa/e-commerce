package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import kotlin.text.isNullOrBlank

object Specifications {

    fun <T : Any> equal(
        field: String,
        value: Any?,
    ): Specification<T> = Specification { root, _, cb ->
        if (value == null) {
            null
        } else {
            cb.equal(root.get<Any>(field), value)
        }
    }

    fun <T : Any> like(
        field: String,
        value: String?
    ): Specification<T> =
        Specification { root, _, cb ->
            if (value.isNullOrBlank()) {
                null
            } else {
                cb.like(
                    // lower convert database value to lowercase
                    // value.lowercase() convert searching value to lowercase
                    cb.lower(root.get(field)),
                    "%${value.lowercase()}%"
                )
            }
        }


}