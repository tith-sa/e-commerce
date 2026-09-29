package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderItemRequest
import spring.ecommerce.dto.request.UpdatedOrderItemRequest
import spring.ecommerce.dto.response.OrderItemResponse
import spring.ecommerce.service.`interface`.OrderItemService


@RestController
@RequestMapping("/api/order-items")
@SecurityRequirement(name = "bearerAuth")
class OrderItemController(
    private val orderItemService: OrderItemService
) {

    @PostMapping("/add-item/{orderId}")
    @Operation(summary = "add new item")
    fun addOrderItem(
        @PathVariable orderId: Long,
        @RequestBody request: OrderItemRequest
    ): ResponseEntity<Response<Unit>>{
        val result = orderItemService.addOrderItem(orderId,request)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "update OrderItem")
    fun updateOrder(
        @PathVariable id: Long,
        @RequestBody request: UpdatedOrderItemRequest
    ): ResponseEntity<Response<OrderItemResponse>> {
        val result = orderItemService.updateOrderItem(id, request)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete OrderItem")
    fun deleteOrderItem(
        @PathVariable id: Long
    ): ResponseEntity<Response<Unit>> {
        val result = orderItemService.deleteOrderItemById(id)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a OrderItem")
    fun viewOrderItem(
        @PathVariable id: Long): ResponseEntity<Response<OrderItemResponse>> {
        val result = orderItemService.viewOrderItem(id)
        return ResponseEntity.ok(result)
    }
}