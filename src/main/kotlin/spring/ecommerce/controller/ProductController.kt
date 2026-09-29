package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.annotaion.RequirePermission
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.ProductRequest
import spring.ecommerce.dto.request.SearchProductRequest
import spring.ecommerce.dto.request.UpdatedProductRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.ProductResponse
import spring.ecommerce.service.`interface`.ProductService


@RestController
@RequestMapping("/api/products")
@SecurityRequirement(name = "bearerAuth")
class ProductController(
    private val productService: ProductService
) {


    @PostMapping("/create")
    @Operation(summary = "Create a new product")
    @RequirePermission("CREATE_PRODUCT")
    fun postProduct(
        @Valid
        @RequestAttribute userId : Long,
        @RequestBody request: ProductRequest,
    ): ResponseEntity<Response<Unit>> {
        val result = productService.createProduct(userId ,request)
        return ResponseEntity.ok(result)
    }


    @GetMapping
    @Operation(summary = "Get all products")
    fun listProducts(
        @Valid
        @ModelAttribute search: SearchProductRequest,
        @ModelAttribute requestPagination: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<ProductResponse>>>{
        val result = productService.listProducts(search,requestPagination)
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Update a product")
    @RequirePermission("UPDATE_PRODUCT")
    fun updatedProduct(
        @Valid
        @PathVariable id: Long,
        @RequestBody request: UpdatedProductRequest,
    ) : ResponseEntity<Response<Unit>>{
        val result = productService.updateProduct(id ,request)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get detail product")
    fun viewProduct(
        @PathVariable id: Long
    ): ResponseEntity<Response<ProductResponse>> {
        val result = productService.viewProduct(id)
        return ResponseEntity.ok(result)
    }


}