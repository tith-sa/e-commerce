package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.annotaion.RequirePermission
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchUserRequest
import spring.ecommerce.dto.request.UpdatedUserRequest
import spring.ecommerce.dto.request.UserRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.UserResponse
import spring.ecommerce.service.`interface`.UserService

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
class UserController(
    val userService: UserService
) {

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Create a new user")
    @RequirePermission("CREATE_USER")
    fun createUser(
        @Valid
        @RequestBody request: UserRequest
    ): ResponseEntity<Response<Unit>> {
        val result = userService.create(request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    @Operation(summary = "Get all users")
    @RequirePermission("LIST_USERS")
    fun listUsers(
        @Valid
        @ModelAttribute search: SearchUserRequest,
        @ModelAttribute requestPagination: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<UserResponse>>> {
        val result = userService.listUsers(search, requestPagination)
        return ResponseEntity.ok(result)
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    @RequirePermission("VIEW_USER")
    fun viewUser(
        @PathVariable id: Long): ResponseEntity<Response<UserResponse>> {
        val result = userService.viewUser(id)
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Update user by id")
    @RequirePermission("UPDATE_USER")
    fun updatedUser(
        @Valid
        @PathVariable id: Long,
        @RequestBody request: UpdatedUserRequest
    ):ResponseEntity<Response<Unit>>{
        val result = userService.updateUser(id, request)
        return ResponseEntity.ok(result)
    }


    @PatchMapping("/is-deleted/{id}")
    @Operation(summary = "Soft delete user by id")
    @RequirePermission("DELETE_USER")
    fun updatedIsUserDeleted(
        @PathVariable id: Long
    ): ResponseEntity<Response<Unit>> {
        val result = userService.deletedUser(id)
        return ResponseEntity.ok(result)
    }
}