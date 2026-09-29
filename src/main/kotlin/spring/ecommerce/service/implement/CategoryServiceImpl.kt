package spring.ecommerce.service.implement

import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CategoryRequest
import spring.ecommerce.dto.response.CategoryResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Category
import spring.ecommerce.repository.CategoryRepository
import spring.ecommerce.repository.specification.categorySpecification
import spring.ecommerce.service.`interface`.CategoryService

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {
    override fun create(request: CategoryRequest): Response<Unit> {
        val (name, description) = request

        if (categoryRepository.existsByName(name)) {
            throw BadRequestException("Category already exists")
        }

        val category = Category(
            name = name,
            description = description
        )
        categoryRepository.save(category)

        return Response(
            status = HttpStatus.CREATED,
            data = null,
            message = "Category created"
        )
    }

    override fun listCategories(
        name: String?,
    ): Response<List<CategoryResponse>>{
        val specification = categorySpecification(name)
        val categories = categoryRepository.findAll(specification)

        val response =  categories.map {
            CategoryResponse(
                id = it.id,
                name = it.name,
                description = it.description,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Category returned"
        )
    }

    override fun updateCategory(
        id:Long,
        request: CategoryRequest
    ): Response<Unit>{
        val(name) = request
        val category = categoryRepository.findById(id).orElseThrow {
            throw NotFoundException("Category not found")
        }

        if(categoryRepository.existsByName(name)){
            throw BadRequestException("Category already exists")
        }

        category.name = name
        categoryRepository.save(category)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Category updated"
        )

    }

    override fun deleteCategory(id: Long): Response<Unit> {
        val category = categoryRepository.findById(id).orElseThrow{
            throw NotFoundException("Category not found")
        }

        categoryRepository.delete(category)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Category deleted"
        )

    }

    override fun viewCategory(id: Long): Response<CategoryResponse> {
        val category = categoryRepository.findById(id).orElseThrow {
            NotFoundException("Category not found")
        }
        val response = CategoryResponse(
            id = category.id,
            name = category.name,
            description = category.description,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Category returned"
        )
    }
}