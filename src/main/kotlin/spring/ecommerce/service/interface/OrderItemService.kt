package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderItemRequest
import spring.ecommerce.dto.response.OrderItemResponse

interface OrderItemService {
    fun createOrderItem(orderId: Long, productId: Long, request: OrderItemRequest ): OrderItemResponse
}