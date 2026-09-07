package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CategoryRequest
import spring.ecommerce.dto.response.CategoryResponse
import spring.ecommerce.service.`interface`.CategoryService


@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping("/create")
    @Operation(summary = "Creates new Category")
    fun create(
        @Valid
        @RequestBody request: CategoryRequest
    ) : ResponseEntity<Response<CategoryResponse>> {
        val result = categoryService.create(request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    @Operation(summary = "Get all Categories")
    fun getAllCategories() : ResponseEntity<Response<List<CategoryResponse>>>{
        val result = categoryService.getAllCategories()
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Update Category")
    fun updatedCategory(
        @Valid
        @PathVariable id : Long,
        @RequestBody request: CategoryRequest
    ): ResponseEntity<Response<CategoryResponse>> {
        val result = categoryService.updateCategory(id,request)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/search")
    @Operation(summary = "Search by name")
    fun searchCategoriesByName(
        @RequestParam name: String
    ) : ResponseEntity<Response<List<CategoryResponse>>>{
        val result = categoryService.searchCategoryByName(name)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Category")
    fun deleteCategory(
        @PathVariable id:Long
    ): ResponseEntity<Response<Unit>>{
        val result = categoryService.deleteCategory(id)
        return ResponseEntity.ok(result)
    }

}