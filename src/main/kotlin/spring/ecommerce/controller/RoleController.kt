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
import org.springframework.web.bind.annotation.RestController
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.RoleRequest
import spring.ecommerce.dto.request.UpdatedRoleRequest
import spring.ecommerce.dto.response.RoleResponse
import spring.ecommerce.service.`interface`.RoleService

@RestController
@RequestMapping("/api/roles")
class RoleController(
    private val roleService: RoleService
) {

    @PostMapping("/create")
    @Operation(summary = "Creates new Role")
    fun createRole(
        @Valid
        @RequestBody request: RoleRequest
    ): ResponseEntity<Response<RoleResponse>>{
        val result = roleService.createRole(request)
        return ResponseEntity.ok(result)
    }


    @GetMapping("/list")
    @Operation(summary = "Get all Role")
    fun getAllRoles(): ResponseEntity<Response<List<RoleResponse>>>{
        val result = roleService.getAllRole()
        return ResponseEntity.ok(result)
    }


    @PutMapping("/update/{roleId}")
    @Operation(summary = "Update role")
    fun updatedRole(
        @PathVariable roleId: Long,
        @RequestBody request: UpdatedRoleRequest
    ): ResponseEntity<Response<RoleResponse>>{
        val result = roleService.updateRole(roleId, request)
        return ResponseEntity.ok(result)
    }


    @DeleteMapping("/delete/{roleId}")
    @Operation(summary = "Delete role")
    fun deletedRole(
        @PathVariable roleId: Long
    ): ResponseEntity<Response<Unit>>{
        val result = roleService.deleteRole(roleId)
        return ResponseEntity.ok(result)
    }
}