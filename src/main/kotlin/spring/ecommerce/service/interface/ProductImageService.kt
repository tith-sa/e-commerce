package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.ProductImageRequest
import spring.ecommerce.dto.request.UpdatedProductImageRequest
import spring.ecommerce.dto.response.ProductImageResponse

interface ProductImageService {
    fun deleteAllProductImages(productId: Long)
    fun viewProductImage(id: Long): Response<ProductImageResponse>
    fun addNewProductImages(productId: Long, request: ProductImageRequest): Response<ProductImageResponse>
    fun deleteProductImageById(id: Long): Response<Unit>
}