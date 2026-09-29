package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderItemRequest
import spring.ecommerce.dto.request.UpdatedOrderItemRequest
import spring.ecommerce.dto.response.OrderItemResponse

interface OrderItemService {
    fun addOrderItem(orderId: Long, request: OrderItemRequest ): Response<Unit>
    fun deleteOrderItemById(id: Long): Response<Unit>
    fun updateOrderItem(id: Long, request: UpdatedOrderItemRequest): Response<OrderItemResponse>
    fun viewOrderItem(id: Long): Response<OrderItemResponse>
}