package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.ProductRequest
import spring.ecommerce.dto.request.SearchProductRequest
import spring.ecommerce.dto.request.UpdatedProductRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.ProductResponse

interface ProductService {
    fun createProduct(userId: Long,request: ProductRequest): Response<Unit>
    fun updateProduct(id: Long, request: UpdatedProductRequest): Response<Unit>
    fun viewProduct(id: Long): Response<ProductResponse>
    fun listProducts(search: SearchProductRequest, requestPagination: PaginationRequest): Response<PaginationResponse<ProductResponse>>
}