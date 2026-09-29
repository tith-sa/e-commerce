package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.annotaion.RequirePermission
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderRequest
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchOrderRequest
import spring.ecommerce.dto.request.UpdatedOrderStatusRequest
import spring.ecommerce.dto.response.OrderResponse
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.service.`interface`.OrderService


@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "bearerAuth")
class OrderController(
    val orderService: OrderService,
) {


    @PostMapping("/create")
    @Operation(summary = "Create a new order")
    fun createOrder(
        @RequestBody request: OrderRequest
    ): ResponseEntity<Response<Unit>> {
        val result = orderService.createOrder(request)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @RequirePermission("VIEW_ORDER")
    fun viewOrder(
        @PathVariable id: Long
    ): ResponseEntity<Response<OrderResponse>> {
        val result = orderService.viewOrder(id)
        return ResponseEntity.ok(result)
    }


    @GetMapping
    @Operation(summary = "Get all orders")
    @RequirePermission("LIST_ORDER")
    fun listOrders(
        @ModelAttribute search: SearchOrderRequest,
        @ModelAttribute request: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<OrderResponse>>> {
        val result = orderService.listOrders(search,request)
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/update-status/{id}")
    @Operation(summary = "Update a order status")
    @RequirePermission("UPDATE_ORDER_STATUS")
    fun updateOrderStatus(
        @PathVariable id: Long,
        @RequestBody request: UpdatedOrderStatusRequest
    ): ResponseEntity<Response<Unit>>{
        val result = orderService.updateOrderStatus(id, request)
        return ResponseEntity.ok(result)
    }
}