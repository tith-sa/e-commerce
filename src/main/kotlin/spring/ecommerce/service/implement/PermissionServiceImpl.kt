package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.PermissionRequest
import spring.ecommerce.dto.request.UpdatedPermissionRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.PermissionResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Permission
import spring.ecommerce.repository.PermissionRepository
import spring.ecommerce.repository.RolePermissionRepository
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.service.`interface`.PermissionService
import spring.ecommerce.service.`interface`.RolePermissionService
import kotlin.collections.forEach


@Service
class PermissionServiceImpl(
    private val permissionRepository: PermissionRepository,
    private val rolePermissionService: RolePermissionService,
    private val roleRepository: RoleRepository,
    private val rolePermissionRepository: RolePermissionRepository
): PermissionService {

    @Transactional
    override fun postPermission(request: PermissionRequest): Response<PermissionResponse>{
        val (permissionName, description, roles) = request
        if (permissionRepository.existsByPermissionName(permissionName)){
            throw BadRequestException("Permission name already exists")
        }

        val permission = Permission(
            permissionName = permissionName,
            description = description
        )

        val permissionId = permission.id
            ?: throw NotFoundException("Permission ID not found")

        // Assign permission to each role
        roles.forEach { roleName ->

            val role = roleRepository
                .findByNameIgnoreCase(roleName)
                .orElseThrow {
                    NotFoundException("Role name not found: $roleName")
                }.id
                ?: throw NotFoundException("Role ID not found: $roleName")

            rolePermissionService.assignPermission(
                roleId = role,
                permissionId = permissionId
            )
        }

        val response = PermissionResponse(
            id = permission.id,
            permissionName = permission.permissionName,
            description = permission.description,
            roles = roles
        )

        return Response(
            status = HttpStatus.CREATED,
            data = response,
            message = "Permission created"
        )
    }

    @Transactional
    override fun updatePermission(id: Long, request: UpdatedPermissionRequest): Response<PermissionResponse> {
        val ( description, roles) = request
        val permission = permissionRepository.findById(id).orElseThrow {
            NotFoundException("Permission not found")
        }

        description?.let {
            permission.description = description
            println("Updating permission $description")
        }

        roles?.let { roleNames ->
            rolePermissionRepository.deleteAllByPermissionId(id)

            roleNames.distinct().forEach { roleName ->
                val role = roleRepository.findByNameIgnoreCase(roleName)
                    .orElseThrow { NotFoundException("Role name not found: $roleName") }

                rolePermissionService.assignPermission(
                    roleId = role.id ?: throw NotFoundException("Role ID not found"),
                    permissionId = id
                )
            }
        }

        // Get updated roles
        val roleMapping = rolePermissionRepository.findRoleNamesByPermissionId(id)
            .mapNotNull { it.roleId }
            .let { roleIds -> roleRepository.findAllById(roleIds).map { it.name } }

        val response = PermissionResponse(
            id = permission.id,
            permissionName = permission.permissionName,
            description = permission.description,
            roles = roleMapping
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Permission updated"
        )
    }

    override fun getAllPermissions(request: PaginationRequest): Response<PaginationResponse<PermissionResponse>> {
        val (page, size) = request
        val pageable = PageRequest.of(
            page - 1,
            size
        )
        val permissions = permissionRepository.findAll(pageable)

        val permissionMapping = permissions.content.map{ permission ->

            val permissionId = permission.id
            ?: throw NotFoundException("Permission ID not found")
            val roleMapping = rolePermissionRepository.findRoleNamesByPermissionId(permissionId)
                .mapNotNull { it.roleId }
                .let { roleIds -> roleRepository.findAllById(roleIds).map { it.name } }

            PermissionResponse(
                id = permission.id,
                permissionName = permission.permissionName,
                description = permission.description,
                roles = roleMapping
            )
        }

        val pagination = PaginationResponse(
            meta = PaginationResponse.ResponsePageMeta(
                page = page,
                pageSize = size,
                totalElements = permissions.totalElements,
                totalPages = permissions.totalPages,
            ),
            contents = permissionMapping,
        )

        return Response(
            status = HttpStatus.OK,
            data = pagination,
            message = "Retrieved all permission"
        )
    }
}