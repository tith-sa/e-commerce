package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.ProductRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.ProductResponse
import spring.ecommerce.service.`interface`.ProductService


@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {


    @PostMapping("/create")
    @Operation(summary = "Create a new product")
    fun postProduct(
        @Valid
        @RequestAttribute userId : Long,
        @RequestBody request: ProductRequest,
    ): ResponseEntity<Response<ProductResponse>> {
        val result = productService.createProduct(userId ,request)
        return ResponseEntity.ok(result)
    }


    @GetMapping
    @Operation(summary = "Get all products")
    fun getAllProducts(
        @Valid
        @ModelAttribute request: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<ProductResponse>>>{
        val result = productService.getAllProducts(request)
        return ResponseEntity.ok(result)
    }

}