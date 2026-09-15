package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.PermissionRequest
import spring.ecommerce.dto.request.UpdatedPermissionRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.PermissionResponse

interface PermissionService {
    fun postPermission(request: PermissionRequest): Response<PermissionResponse>
    fun updatePermission(id: Long, request: UpdatedPermissionRequest): Response<PermissionResponse>
    fun getAllPermissions(request: PaginationRequest): Response<PaginationResponse<PermissionResponse>>
}