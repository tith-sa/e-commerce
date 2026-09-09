package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.response.OrderResponse
import spring.ecommerce.model.Product

interface OrderService {
    fun createOrder(customerId: Long ): Response<OrderResponse>
}