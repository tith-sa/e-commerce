package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.ProductRequest
import spring.ecommerce.dto.request.SearchProductRequest
import spring.ecommerce.dto.request.UpdatedProductRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.ProductResponse

interface ProductService {
    fun createProduct(userId: Long,request: ProductRequest): Response<ProductResponse>
    fun getAllProducts(request: PaginationRequest): Response<PaginationResponse<ProductResponse>>
    fun updateProduct(id: Long, request: UpdatedProductRequest): Response<ProductResponse>
    fun deleteProduct(id: Long): Response<Unit>
    fun getProductById(id: Long): Response<ProductResponse>
    fun searchProducts(request: SearchProductRequest, requestPagination: PaginationRequest): Response<PaginationResponse<ProductResponse>>
}