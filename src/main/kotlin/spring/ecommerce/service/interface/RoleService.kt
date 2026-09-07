package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.RoleRequest
import spring.ecommerce.dto.request.UpdatedRoleRequest
import spring.ecommerce.dto.response.RoleResponse

interface RoleService {
    fun createRole(request: RoleRequest): Response<RoleResponse>
    fun getAllRole(): Response<List<RoleResponse>>
    fun updateRole(roleId: Long ,request: UpdatedRoleRequest): Response<RoleResponse>
    fun deleteRole(roleId: Long): Response<Unit>
}