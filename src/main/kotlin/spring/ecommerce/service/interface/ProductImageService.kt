package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.ProductImageRequest
import spring.ecommerce.dto.request.UpdatedProductImageRequest
import spring.ecommerce.dto.response.ProductImageResponse

interface ProductImageService {

    fun createImages(productId: Long, images: List<ProductImageRequest>): List<ProductImageResponse>
    fun deleteAllProductImages(productId: Long)
    fun getProductImages(productId: Long): List<ProductImageResponse>
    fun getProductImageById(id: Long): Response<ProductImageResponse>
    fun updateProductImages(id: Long, request: UpdatedProductImageRequest): Response<ProductImageResponse>
}