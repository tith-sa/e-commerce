package spring.ecommerce.service.implement

import org.springframework.stereotype.Service
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.RolePermission
import spring.ecommerce.repository.PermissionRepository
import spring.ecommerce.repository.RolePermissionRepository
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.service.`interface`.RolePermissionService

@Service
class RolePermissionServiceImpl(
    private val permissionRepository: PermissionRepository,
    private val rolePermissionRepository: RolePermissionRepository,
    private val roleRepository: RoleRepository,
): RolePermissionService {

    override fun assignPermission( permissionId : Long, roleId : Long): RolePermission{
        permissionRepository.findById(permissionId).orElseThrow {
            NotFoundException("Permission not found")
        }
        roleRepository.findById(roleId).orElseThrow {
            NotFoundException("Role not found")
        }
        if (rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            throw BadRequestException ("Role permission already exists")
        }

        val assign = RolePermission(
            roleId = roleId,
            permissionId = permissionId,
        )

        return rolePermissionRepository.save(assign)
    }

    override fun hasPermission(roleId: Long, permissionName: String): Boolean{
        val permission = permissionRepository.findByPermissionName(permissionName).orElseThrow {
            NotFoundException("Permission $permissionName not found")
        }
        val permissionId = permission.id
            ?: throw NotFoundException("Permission $permissionName not found")

        return rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)
    }

}