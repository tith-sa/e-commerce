package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderRequest
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchOrderRequest
import spring.ecommerce.dto.request.UpdatedOrderStatusRequest
import spring.ecommerce.dto.response.OrderResponse
import spring.ecommerce.dto.response.PaginationResponse

interface OrderService {
    fun createOrder(request: OrderRequest ): Response<Unit>
    fun viewOrder(id: Long): Response<OrderResponse>
    fun listOrders(search: SearchOrderRequest, request: PaginationRequest): Response<PaginationResponse<OrderResponse>>
    fun updateOrderStatus(id: Long, request: UpdatedOrderStatusRequest): Response<Unit>
}