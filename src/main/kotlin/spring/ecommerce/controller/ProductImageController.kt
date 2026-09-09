package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
class ProductImageController(
    val productImageService: ProductImageService
){


//    fun createProductImage(
//        @PathVariable productId: Long,
//        @RequestBody
//    ): ResponseEntity<Response<List<<ProductResponse>>> {
//
//    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a product image")
    fun getProductImageById(
        @PathVariable id: Long
    ): ResponseEntity<Response<ProductImageResponse>>{
        val result = productImageService.getProductImageById(id)
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Update a product image")
    fun updatedProductImage(
        @PathVariable id: Long,
        @RequestBody request: UpdatedProductImageRequest
    ): ResponseEntity<Response<ProductImageResponse>>{
        val result = productImageService.updateProductImages(id, request)
        return ResponseEntity.ok(result)
    }
}
