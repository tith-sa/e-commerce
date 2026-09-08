package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CustomerRequest
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchCustomerRequest
import spring.ecommerce.dto.request.UpdatedCustomerRequest
import spring.ecommerce.dto.response.CustomerResponse
import spring.ecommerce.dto.response.PaginationResponse
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


    @GetMapping
    @Operation(summary = "Get all customers")
    fun getAllCustomers(
        @ModelAttribute request: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<CustomerResponse>>> {
        val result = customerService.getAllCustomers(request)
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{customerId}")
    @Operation(summary = "Update a customer")
    fun updatedCustomer(
        @PathVariable customerId: Long,
        @RequestBody request: UpdatedCustomerRequest
    ): ResponseEntity<Response<CustomerResponse>>{
        val result = customerService.updateCustomer(customerId, request)
        return ResponseEntity.ok(result)
    }


    @PatchMapping("/isDelete/{id}")
    @Operation(summary = "Soft delete a customer")
    fun isDeletedCustomer(
       @PathVariable id: Long
    ): ResponseEntity<Response<CustomerResponse>> {
        val result = customerService.updatedIsCustomerDeleted(id)
        return ResponseEntity.ok(result)
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get customer details")
    fun getCustomerById(
        @PathVariable id: Long
    ): ResponseEntity<Response<CustomerResponse>> {
        val result = customerService.getCustomerById(id)
        return ResponseEntity.ok(result)
    }


    @GetMapping("/search")
    @Operation(summary = "Search customers")
    fun searchCustomers(
        @ModelAttribute request: SearchCustomerRequest,
        @ModelAttribute requestPagination: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<CustomerResponse>>> {
        val result = customerService.searchCustomer(request, requestPagination)
        return ResponseEntity.ok(result)
    }
}