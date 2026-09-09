package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
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
}