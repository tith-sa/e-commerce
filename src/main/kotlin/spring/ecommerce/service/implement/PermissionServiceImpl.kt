package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
import spring.ecommerce.model.RolePermission
import spring.ecommerce.repository.PermissionRepository
import spring.ecommerce.repository.RolePermissionRepository
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.service.`interface`.PermissionService


@Service
class PermissionServiceImpl(
    private val permissionRepository: PermissionRepository,
    private val roleRepository: RoleRepository,
    private val rolePermissionRepository: RolePermissionRepository
): PermissionService {

    @Transactional
    override fun postPermission(request: PermissionRequest): Response<Unit>{
        val (parentId, code, sortOrder, name, roles) = request // destructuring

        // exist code
        if (permissionRepository.existsByCode(code)){
            throw BadRequestException("Permission code already exists")
        }

        // set roles and validate
        val setRoles = roles.toSet()
        val foundRoles = roleRepository.findByIdIn(setRoles)
        if (foundRoles.size != roles.size){
            throw NotFoundException("Role not found")
        }

        // validate parentId if not null
        parentId?.let {
            permissionRepository.findById(it).orElseThrow {
                NotFoundException("Parent ID not found")
            }
        }

        // permission name store uppercase
        // create permission
        val permission = Permission(
            parentId = parentId,
            code = code,
            name = name
        )

        // save it to db
        permissionRepository.save(permission)

        // create mutable list of role_permission
        val rolePermissions = mutableListOf<RolePermission>()
        var currentSortOrder = sortOrder - 1

        for (role in foundRoles){

            val roleId = role.id
                ?: throw NotFoundException("Role not found")
            val permissionId = permission.id
                ?: throw NotFoundException("Permission not found")

            // exist role permission
            if (rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
                throw BadRequestException ("Role permission already exists")
            }
            currentSortOrder += 1

            // add role permission to mutable list
            rolePermissions.add(
                RolePermission(
                    roleId = roleId,
                    permissionId = permissionId,
                    sortOrder = currentSortOrder,
                )
            )
        }

        // save all role permission
        rolePermissionRepository.saveAll(rolePermissions)


        return Response(
            status = HttpStatus.CREATED,
            data = null,
            message = "Permission created"
        )
    }

    @Transactional
    override fun updatePermission(id: Long, request: UpdatedPermissionRequest): Response<Unit> {
        val (parentId, name, roles) = request

        val permission = permissionRepository.findById(id).orElseThrow {
            NotFoundException("Permission ID not found")
        }

        parentId?.let {
            permissionRepository.findByParentId(it).orElseThrow {
                NotFoundException("Parent ID not found")
            }
            permission.parentId = it
        }

        name?.let {
            permission.name = it
        }


        roles?.let {

            val setRoles = roles.toSet()
            val foundRoles = roleRepository.findByIdIn(setRoles)
            if (foundRoles.size != setRoles.size) {
                throw BadRequestException("Role permission already exists")
            }

            val rolePermissions = mutableListOf<RolePermission>()


            for (role in foundRoles) {

                val roleId = role.id
                    ?: throw NotFoundException("Role not found")
                val permissionId = permission.id
                    ?: throw NotFoundException("Permission not found")

                // exist role permission
                if (rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
                    throw BadRequestException("Role permission already exists")
                }

                // add role permission to mutable list
                rolePermissions.add(
                    RolePermission(
                        roleId = roleId,
                        permissionId = permissionId,
                    )
                )
            }
        }


            return Response(
                status = HttpStatus.OK,
                data = null,
                message = "Permission updated"
            )
        }

        override fun listPermissions(request: PaginationRequest): Response<PaginationResponse<PermissionResponse>> {
            val (page, size) = request
            val pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.ASC, "id")
            )

            val permissions = permissionRepository.findAll(pageable)

            val permissionMapping = permissions.content.map { permission ->

                PermissionResponse(
                    id = permission.id,
                    parentId = permission.parentId,
                    code = permission.code,
                    name = permission.name,
                    createdAt = permission.createdAt,
                    updatedAt = permission.updatedAt,
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

    @Transactional
    override fun deletePermission(id: Long): Response<Unit> {
        val permission = permissionRepository.findById(id).orElseThrow {
            NotFoundException("Permission not found")
        }
        rolePermissionRepository.deleteAllByPermissionId(id)
        permissionRepository.delete(permission)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Permission deleted"
        )
    }

}