package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.RoleRequest
import spring.ecommerce.dto.request.UpdatedRoleRequest
import spring.ecommerce.dto.response.RoleResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Role
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.service.`interface`.RoleService

@Service
class RoleServiceImpl(
    private val roleRepository: RoleRepository
) : RoleService {

    override fun createRole(request: RoleRequest): Response<RoleResponse> {
        val (name, description) = request

        if (roleRepository.existsByNameIgnoreCase(name)){
             throw BadRequestException("Role already exists")
            }

        val role = Role(
            name = name.uppercase(),
            description = description
        )

        roleRepository.save(role)

        val response = RoleResponse(
            id = role.id,
            name = role.name,
            description = role.description
        )

        return Response(
            status = HttpStatus.CREATED,
            data = response,
            message = "Successfully created new role."
        )
    }

    override fun getAllRole(): Response<List<RoleResponse>> {
        val role = roleRepository.findAll()

        val response = role.map {
            RoleResponse(
                id = it.id,
                name = it.name,
                description = it.description
            )
        }

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Successfully created new role."
        )
    }

    @Transactional
    override fun updateRole(roleId: Long, request: UpdatedRoleRequest): Response<RoleResponse> {
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

        val response = RoleResponse(
            id = role.id,
            name = role.name,
            description = role.description
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Role updated."
        )
    }

    override fun deleteRole(roleId: Long): Response<Unit>{
        val role = roleRepository.findById(roleId).orElseThrow {
            throw NotFoundException("Role not found")
        }

        roleRepository.delete(role)
        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Role deleted."
        )
    }
}