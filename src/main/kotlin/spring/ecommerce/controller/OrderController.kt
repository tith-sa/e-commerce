package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.response.OrderResponse
import spring.ecommerce.service.`interface`.OrderService


@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "bearerAuth")
class OrderController(
    val orderService: OrderService,
) {


    @PostMapping("/create/{customerId}")
    @Operation(summary = "Create a new order")
    fun createOrder(
        @PathVariable customerId: Long
    ): ResponseEntity<Response<OrderResponse>> {
        val result = orderService.createOrder(customerId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getOrderBYId(
        @PathVariable id: Long
    ): ResponseEntity<Response<OrderResponse>> {
        val result = orderService.getOrderById(id)
        return ResponseEntity.ok(result)
    }
}