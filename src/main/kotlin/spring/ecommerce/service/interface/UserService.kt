package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchUserRequest
import spring.ecommerce.dto.request.UpdatedUserRequest
import spring.ecommerce.dto.request.UpdatedUserRoleRequest
import spring.ecommerce.dto.request.UserRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.UserResponse

interface UserService {
    fun create(request: UserRequest): Response<UserResponse>
    fun getAllUsers(request: PaginationRequest): Response<PaginationResponse<UserResponse>>
    fun getUserById(id: Long): Response<UserResponse>
    fun updateUser(id: Long, request: UpdatedUserRequest): Response<UserResponse>
    fun updateUserRole(id: Long, request: UpdatedUserRoleRequest): Response<UserResponse>
    fun updatedIsUserDeleted(id: Long): Response<UserResponse>
    fun searchUser(request: SearchUserRequest, requestPagination: PaginationRequest): Response<PaginationResponse<UserResponse>>
}
