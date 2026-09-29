package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import kotlin.reflect.KClass
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

//    fun <T: Any,R : Any> likeById(
//        idField: String,
//        relatedEntity:KClass<R>,
//        relatedIdField: String,
//        relatedNameField: String,
//        value: String?
//    ): Specification<T> {
//        return Specification { root, query, cb ->
//
//            if (value.isNullOrBlank()) {
//                return@Specification null
//            }
//
//            val subquery = query.subquery(Long::class.java)
//            val related = subquery.from(relatedEntity.java)
//
//            subquery
//                .select(related.get<Long>(relatedIdField))
//                .where(
//                    cb.like(
//                        cb.lower(related.get(relatedNameField)),
//                        "%${value.lowercase()}%"
//                    )
//                )
//
//            root.get<Long>(idField).`in`(subquery)
//        }
//    }

    fun <T : Any, Y : Comparable<Y>> greaterThan(
        field: String,
        value: Y?
    ): Specification<T> =
        Specification { root, _, cb ->
            if (value == null) {
                null
            } else {
                cb.greaterThan(root.get<Y>(field), value)
            }
        }

    fun <T : Any, Y : Comparable<Y>> lessThan(
        field: String,
        value: Y?
    ): Specification<T> =
        Specification { root, _, cb ->
            if (value == null) {
                null
            } else {
                cb.lessThan(root.get<Y>(field), value)
            }
        }


}