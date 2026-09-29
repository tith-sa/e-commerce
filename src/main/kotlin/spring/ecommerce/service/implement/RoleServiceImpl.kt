package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.RoleRequest
import spring.ecommerce.dto.request.UpdatedRoleRequest
import spring.ecommerce.dto.response.PermissionResponse
import spring.ecommerce.dto.response.RoleResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Role
import spring.ecommerce.repository.PermissionRepository
import spring.ecommerce.repository.RolePermissionRepository
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.service.`interface`.RoleService

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository,
    private val rolePermissionRepository: RolePermissionRepository,
    private val permissionRepository: PermissionRepository
) : RoleService {

    override fun createRole(request: RoleRequest): Response<Unit> {
        val (name, description) = request

        if (roleRepository.existsByNameIgnoreCase(name)){
             throw BadRequestException("Role already exists")
            }

        val role = Role(
            name = name.uppercase(),
            description = description
        )

        roleRepository.save(role)

        return Response(
            status = HttpStatus.CREATED,
            data = null,
            message = "Created new role."
        )
    }

    override fun viewRole(id: Long): Response<RoleResponse> {
        val role = roleRepository.findById(id).orElseThrow {
            NotFoundException("Role not found")
        }

        val roleId = role.id
            ?: throw NotFoundException("Role not found")
        val rolePermissions = rolePermissionRepository.findByRoleId(roleId)

        val permissionIds = rolePermissions.map { it.id }.toSet()
        val permissions = permissionRepository.findByIdIn(permissionIds)

        val responsePermissions = permissions.map {
            PermissionResponse(
                id = it.id,
                parentId = it.parentId,
                code = it.code,
                name = it.name,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
            )
        }

        val response = RoleResponse(
            id = role.id,
            name = role.name,
            description = role.description,
            permissions = responsePermissions,
            createdAt = role.createdAt,
            updatedAt = role.updatedAt
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "View role."
        )
    }

    override fun listRoles(): Response<List<RoleResponse>> {

        val roles = roleRepository.findAllByOrderByIdAsc()

        val roleIds = roles.map { it.id }
        val rolePermissions = rolePermissionRepository.findByRoleIdIn(roleIds)

        val permissionIds = rolePermissions.map { it.id }.toSet()
        val permissions = permissionRepository.findByIdIn(permissionIds)

        val response = roles.map { role ->
            val filterRolePermissions =  rolePermissions.filter{ it.roleId == role.id }
            val rolePermissionIds = filterRolePermissions.map { it.permissionId }.toSet()

            val filterPermissions = permissions.filter {
                it.id in rolePermissionIds
            }

            val responsePermissions = filterPermissions.map {
                PermissionResponse(
                    id = it.id,
                    parentId = it.parentId,
                    code = it.code,
                    name = it.name,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                )
            }

            RoleResponse(
                id = role.id,
                name = role.name,
                description = role.description,
                permissions = responsePermissions,
                createdAt = role.createdAt,
                updatedAt = role.updatedAt,
            )
        }

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Retrieved roles"
        )
    }

    @Transactional
    override fun updateRole(roleId: Long, request: UpdatedRoleRequest): Response<Unit> {
        val (name, description) = request

        val role = roleRepository.findById(roleId).orElseThrow {
            throw NotFoundException("Role not found")
        }

        name?.let {
            role.name = it
        }

        description?.let {
            role.description = it
        }

        roleRepository.save(role)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Role updated."
        )
    }
}