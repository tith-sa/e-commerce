package spring.ecommerce.aspect

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import spring.ecommerce.annotaion.RequirePermission
import spring.ecommerce.handleException.ForbiddenException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.repository.PermissionRepository
import spring.ecommerce.repository.RolePermissionRepository
import spring.ecommerce.repository.UserRepository


@Aspect
@Component
class CheckPermission(
    private val userRepository: UserRepository,
    private val request: HttpServletRequest,
    private val rolePermissionRepository: RolePermissionRepository,
    private val permissionRepository: PermissionRepository
) {

    @Around("@annotation(requirePermission)")
    fun checkPermission(
        joinPoint: ProceedingJoinPoint,
        requirePermission: RequirePermission
    ):Any {

        val userId = request.getAttribute("userId") as Long

        val user = userRepository.findById(userId).orElseThrow {
            NotFoundException("User not found")
        }

        val roleId = user.roleId
            ?: throw NotFoundException("Role not found")

        val setCode = requirePermission.code.toSet()
        val permissions = permissionRepository.findByCodeIn(setCode)
        if (permissions.size != setCode.size) {
            throw NotFoundException("Permissions not found")
        }

        for (code in setCode) {
            val permission = permissions.find{ it.code == code }
                ?: throw NotFoundException("Permission not found")
            val permissionId = permission.id
                ?: throw NotFoundException("Permission not found")

            val hasPermission = rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)

            if (!hasPermission) {
                throw ForbiddenException("Forbidden")
            }
        }

        return joinPoint.proceed()
    }
}