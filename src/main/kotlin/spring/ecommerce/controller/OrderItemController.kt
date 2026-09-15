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
import spring.ecommerce.dto.response.OrderItemResponse
import spring.ecommerce.model.OrderItem
import spring.ecommerce.service.`interface`.OrderItemService


@RestController
@RequestMapping("/api/order-items")
@SecurityRequirement(name = "bearerAuth")
class OrderItemController(
    private val orderItemService: OrderItemService
) {

    @PostMapping("/{orderId}/item/{productId}")
    @Operation(summary = "create a new OrderItem")
    fun createOrderItem(
        @PathVariable orderId: Long,
        @PathVariable productId: Long,
        @RequestBody request: OrderItemRequest
    ): ResponseEntity<Response<OrderItemResponse>> {
        val result = orderItemService.createOrderItem(orderId, productId, request)
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "update OrderItem")
    fun updateOrder(
        @PathVariable id: Long,
        @RequestBody request: OrderItemRequest
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
    fun getOrderItemById(
        @PathVariable id: Long): ResponseEntity<Response<OrderItemResponse>> {
        val result = orderItemService.getOrderItemById(id)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get All OrderItem By ProductId")
    fun getAllOrderItemsByProductId(
        @PathVariable productId: Long
    ): ResponseEntity<Response<List<OrderItemResponse>>>{
        val result = orderItemService.getAllOrderItemByProductId(productId)
        return ResponseEntity.ok(result)
    }
}