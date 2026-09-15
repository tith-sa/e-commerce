package spring.ecommerce.aspect

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import spring.ecommerce.annotaion.RequirePermission
import spring.ecommerce.handleException.ForbiddenException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.service.`interface`.RolePermissionService


@Aspect
@Component
class CheckPermission(
    private val userRepository: UserRepository,
    private val request: HttpServletRequest,
    private val rolePermissionService: RolePermissionService
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
            ?: throw NotFoundException("Rol e not found")

        val hasPermission = rolePermissionService.hasPermission(
            roleId,
            requirePermission.permission
        )

        if (!hasPermission) {
            throw ForbiddenException("Forbidden")
        }

        return joinPoint.proceed()
    }
}