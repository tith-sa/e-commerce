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
import spring.ecommerce.dto.request.ProductImageRequest
import spring.ecommerce.dto.request.UpdatedProductImageRequest
import spring.ecommerce.dto.response.ProductImageResponse

import spring.ecommerce.service.`interface`.ProductImageService


@RestController
@RequestMapping("/api/productImages")
@SecurityRequirement(name = "bearerAuth")
class ProductImageController(
    val productImageService: ProductImageService
){

    @PostMapping("/add/{productId}")
    @Operation(summary = "Add new product image")
    fun addProductImage(
        @PathVariable productId: Long,
        @RequestBody request: ProductImageRequest
    ): ResponseEntity<Response<ProductImageResponse>>{
        val result = productImageService.addNewProductImages(productId, request)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product image")
    fun viewProductImage(
        @PathVariable id: Long
    ): ResponseEntity<Response<ProductImageResponse>>{
        val result = productImageService.viewProductImage(id)
        return ResponseEntity.ok(result)
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a product image")
    fun deleteProductImageById(
        @PathVariable id: Long): ResponseEntity<Response<Unit>>{
        val result = productImageService.deleteProductImageById(id)
        return ResponseEntity.ok(result)
    }
}
