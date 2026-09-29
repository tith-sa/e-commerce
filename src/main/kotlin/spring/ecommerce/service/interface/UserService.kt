package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchUserRequest
import spring.ecommerce.dto.request.UpdatedUserRequest
import spring.ecommerce.dto.request.UserRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.UserResponse

interface UserService {
    fun create(request: UserRequest): Response<Unit>
    fun viewUser(id: Long): Response<UserResponse>
    fun updateUser(id: Long, request: UpdatedUserRequest): Response<Unit>
    fun deletedUser(id: Long): Response<Unit>
    fun listUsers(
        search: SearchUserRequest,
        requestPagination: PaginationRequest
    ): Response<PaginationResponse<UserResponse>>
}
