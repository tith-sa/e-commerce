package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CustomerRequest
import spring.ecommerce.dto.response.CustomerResponse
import spring.ecommerce.service.`interface`.CustomerService

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
){

    @PostMapping("/create")
    @Operation(summary = "Create a new customer")
    fun createCustomer(
        @RequestBody request: CustomerRequest
    ): ResponseEntity<Response<CustomerResponse>> {
        val result = customerService.createCustomer(request)
        return ResponseEntity.ok(result)
    }
}