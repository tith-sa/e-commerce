package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CategoryRequest
import spring.ecommerce.dto.response.CategoryResponse

interface CategoryService {
    fun create(request: CategoryRequest): Response<CategoryResponse>
    fun getAllCategories(): Response<List<CategoryResponse>>
    fun updateCategory(id:Long,request: CategoryRequest): Response<CategoryResponse>
    fun searchCategoryByName(name: String): Response<List<CategoryResponse>>
    fun deleteCategory(id:Long): Response<Unit>
}