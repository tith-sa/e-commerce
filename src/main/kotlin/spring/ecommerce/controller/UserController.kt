package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchUserRequest
import spring.ecommerce.dto.request.UpdatedUserRequest
import spring.ecommerce.dto.request.UpdatedUserRoleRequest
import spring.ecommerce.dto.request.UserRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.UserResponse
import spring.ecommerce.service.`interface`.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    val userService: UserService
) {

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Create a new user")
    fun createUser(
        @Valid
        @RequestBody request: UserRequest
    ): ResponseEntity<Response<UserResponse>> {
        val result = userService.create(request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    @Operation(summary = "Get all users")
    fun getAllUsers(
        @Valid
        @ModelAttribute request: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<UserResponse>>> {
        val result = userService.getAllUsers(request)
        return ResponseEntity.ok(result)
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    fun getUserById(
        @PathVariable id: Long): ResponseEntity<Response<UserResponse>> {
        val result = userService.getUserById(id)
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Update user by id")
    fun updatedUser(
        @Valid
        @PathVariable id: Long,
        @RequestBody request: UpdatedUserRequest
    ):ResponseEntity<Response<UserResponse>>{
        val result = userService.updateUser(id, request)
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{id}/role")
    @Operation(summary = "Update user role")
    fun updateUserRole(
        @Valid
        @PathVariable id: Long,
        @RequestBody request: UpdatedUserRoleRequest
    ): ResponseEntity<Response<UserResponse>> {
        val result = userService.updateUserRole(id, request)
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/is-deleted/{id}")
    @Operation(summary = "Soft delete user by id")
    fun updatedIsUserDeleted(
        @PathVariable id: Long
    ): ResponseEntity<Response<UserResponse>> {
        val result = userService.updatedIsUserDeleted(id)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/search")
    @Operation(summary = "Search users")
    fun searchUsers(
        @ModelAttribute request: SearchUserRequest,
        @ModelAttribute requestPagination: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<UserResponse>>> {
        val result = userService.searchUser(request,requestPagination)
        return ResponseEntity.ok(result)
    }
}