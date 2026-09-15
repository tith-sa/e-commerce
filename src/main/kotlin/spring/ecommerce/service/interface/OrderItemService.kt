package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderItemRequest
import spring.ecommerce.dto.response.OrderItemResponse

interface OrderItemService {
    fun createOrderItem(orderId: Long, productId: Long, request: OrderItemRequest ): Response<OrderItemResponse>
    fun getAllOrderItemsByOrder(orderId: Long): List<OrderItemResponse>
    fun updateOrderItem(id: Long, request: OrderItemRequest ): Response<OrderItemResponse>
    fun deleteOrderItemById(id: Long): Response<Unit>
    fun getOrderItemById(id: Long): Response<OrderItemResponse>
    fun getAllOrderItemByProductId(productId: Long): Response<List<OrderItemResponse>>
}