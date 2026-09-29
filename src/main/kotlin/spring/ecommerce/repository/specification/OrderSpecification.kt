package spring.ecommerce.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.ecommerce.dto.request.SearchOrderRequest
import spring.ecommerce.model.Order
import spring.ecommerce.model.OrderItem
import spring.ecommerce.model.Product


fun orderSpecification(
    search: SearchOrderRequest
): Specification<Order> {

    val (search, customerPhoneNumber, productIds) = search

//    SELECT o.*
//            FROM orders o
//    INNER JOIN order_item oi
//            ON o.id = oi.order_id
//            WHERE oi.product_id IN (1, 2, 3);

    var specification = Specification<Order> { root, query, cb ->
        query.orderBy(
            cb.desc(root.get<String>("createdAt"))
        )
        cb.conjunction()
    }

    if (!search.isNullOrBlank()){

        var searchSpecification = Specifications.equal<Order>("customerPhoneNumber", search)

        search.toLongOrNull()?.let {

            searchSpecification = searchSpecification.or(
                Specification<Order> { root, query, cb ->

                    query.distinct(true)
                    val items = query.from(OrderItem::class.java)
                    cb.and(
                        cb.equal(items.get<Long>("orderId"), root.get<Long>("id")),
                        cb.equal(items.get<Long>("productId"), it)
                    )
                }
            )
        }

        specification = specification.and(searchSpecification)

    }

    if (!customerPhoneNumber.isNullOrBlank()){
        specification = specification
            .and(Specifications.equal<Order>("customerPhoneNumber", customerPhoneNumber))
    }


    if(!productIds.isNullOrEmpty()){
        val productSpecification =  Specification<Order> { root, query, cb ->

            query.distinct(true)
            val items = query.from(OrderItem::class.java)
            cb.and(
                cb.equal(items.get<Long>("orderId"), root.get<Long>("id")),
                items.get<Long>("productId").`in`(productIds),
            )
        }

        specification = specification.and(productSpecification)
    }


    return specification
}