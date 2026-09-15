package spring.ecommerce.controller

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.annotaion.RequirePermission
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.PermissionRequest
import spring.ecommerce.dto.request.UpdatedPermissionRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.PermissionResponse
import spring.ecommerce.service.`interface`.PermissionService

@RestController
@RequestMapping("/api/permissions")
class PermissionController(
    private val permissionService: PermissionService,
) {

    @PostMapping("/create")
    @Operation(summary = "Creates a new permission")
    fun postPermission(
        @Valid
        @RequestBody request: PermissionRequest
    ): ResponseEntity<Response<PermissionResponse>>{
        val result = permissionService.postPermission(request)
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Updates a permission")
    fun updatePermission(
        @PathVariable id: Long,
        @RequestBody request: UpdatedPermissionRequest
    ): ResponseEntity<Response<PermissionResponse>> {
        val result = permissionService.updatePermission(id, request)
        return ResponseEntity.ok(result)
    }


    @GetMapping
    @Operation(summary = "Get all permissions")
    fun getAllPermissions(
        @ModelAttribute request: PaginationRequest
    ): ResponseEntity<Response<PaginationResponse<PermissionResponse>>>{
        val result = permissionService.getAllPermissions(request)
        return ResponseEntity.ok(result)
    }


}