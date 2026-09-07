package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchUserRequest
import spring.ecommerce.dto.request.UpdatedUserRequest
import spring.ecommerce.dto.request.UpdatedUserRoleRequest
import spring.ecommerce.dto.request.UserRequest
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.dto.response.UserResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.User
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.repository.specification.userSpecification
import spring.ecommerce.service.`interface`.UserService


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository
): UserService {

    override fun create(request: UserRequest): Response<UserResponse> {
        val (username, phoneNumber, password, address, isDeleted) = request
        username?.let {
            if(userRepository.existsByUsername(it)) {
                throw BadRequestException("Username already exists")
            }
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw BadRequestException("Phone number already exists")
        }

        val normalizedUsername = username?.lowercase() ?: phoneNumber

        val role = roleRepository.findByNameIgnoreCase("USER")
            .orElseThrow {
                NotFoundException("Role not found.")
            }

        val hashedPassword = passwordEncoder.encode(password)
        val user = User(
            username = normalizedUsername,
            phoneNumber = phoneNumber,
            address = address,
            roleId = role.id,
            password = hashedPassword,
            isDeleted = isDeleted
        )
        userRepository.save(user)

        val response = UserResponse(
            id = user.id,
            username = user.username,
            phoneNumber = user.phoneNumber,
            address = user.address,
            isDeleted = user.isDeleted,
        )

        return Response(
            status = HttpStatus.CREATED,
            data = response,
            message = "User created"
        )
    }

    override fun getAllUsers(request: PaginationRequest): Response<PaginationResponse<UserResponse>> {
        val (page, size) = request

        val pageable = PageRequest.of(
            page - 1,
            size
        )
        val users = userRepository.findAllByOrderByCreatedAtDesc(pageable)
        val mapUser = users.content.map {
            UserResponse(
                id = it.id,
                username = it.username,
                phoneNumber = it.phoneNumber,
                address = it.address,
                isDeleted = it.isDeleted,
            )
        }

        val pagination = PaginationResponse(
            meta = PaginationResponse.ResponsePageMeta(
                page = page,
                pageSize = size,
                totalElements = users.totalElements,
                totalPages = users.totalPages,
            ),
            contents = mapUser,
        )

        return Response(
            status = HttpStatus.OK,
            data = pagination,
            message = "Users returned"
        )
    }

    override fun getUserById(id: Long): Response<UserResponse>{
        val user = userRepository.findById(id).orElseThrow {
            NotFoundException("User not found.")
        }

        val response = UserResponse(
            id = user.id,
            username = user.username,
            phoneNumber = user.phoneNumber,
            address = user.address,
            isDeleted = user.isDeleted,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "User returned"
        )
    }


    @Transactional
    override fun updateUser(id: Long, request: UpdatedUserRequest): Response<UserResponse> {
        val (username, phoneNumber, password, address) = request

        val user = userRepository.findById(id).orElseThrow{
            NotFoundException("User not found.")
        }

        username?.let {
            if(userRepository.existsByUsername(it)) {
                throw BadRequestException("Username already exists")
            }
            user.username = it.lowercase()
        }
        phoneNumber?.let {
            if (userRepository.existsByPhoneNumber(it)) {
                throw BadRequestException("Phone number already exists")
            }
            user.phoneNumber = it
        }

        password?.let {
            user.password = passwordEncoder.encode(it)
        }

        address?.let {
            user.address = address
        }

        userRepository.save(user)

        val response = UserResponse(
            id = user.id,
            username = user.username,
            phoneNumber = user.phoneNumber,
            address = user.address,
            isDeleted = user.isDeleted,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "User updated"
        )
    }

    override fun updateUserRole(id: Long, request: UpdatedUserRoleRequest): Response<UserResponse> {
        val (roleName) = request
        val user = userRepository.findById(id).orElseThrow {
            NotFoundException("User not found.")
        }
        val role = roleRepository.findByNameIgnoreCase(roleName).orElseThrow {
            NotFoundException("Role not found.")
        }

        user.roleId = role.id

        userRepository.save(user)

        val response = UserResponse(
            id = user.id,
            username = user.username,
            phoneNumber = user.phoneNumber,
            address = user.address,
            isDeleted = user.isDeleted,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Updated User role"
        )
    }

    override fun updatedIsUserDeleted(id: Long): Response<UserResponse> {
        val user = userRepository.findById(id).orElseThrow {
            NotFoundException("User not found.")
        }

        user.isDeleted = true
        userRepository.save(user)

        val response = UserResponse(
            id = user.id,
            username = user.username,
            phoneNumber = user.phoneNumber,
            address = user.address,
            isDeleted = user.isDeleted,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "User deleted"
        )
    }

    override fun searchUser(
        request: SearchUserRequest,
        requestPagination: PaginationRequest
    ): Response<PaginationResponse<UserResponse>> {
        val (page, size) = requestPagination
        val pageable = PageRequest.of(
            page - 1,
            size
        )
        val specification = userSpecification(request)
        val users = userRepository.findAll(specification, pageable)

        val mapUser = users.content.map {
            UserResponse(
                id = it.id,
                username = it.username,
                phoneNumber = it.phoneNumber,
                address = it.address,
                isDeleted = it.isDeleted,
            )
        }

        val pagination = PaginationResponse(
            meta = PaginationResponse.ResponsePageMeta(
                page = page,
                pageSize = size,
                totalElements = users.totalElements,
                totalPages = users.totalPages,
            ),
            contents = mapUser
        )

        return Response(
            status = HttpStatus.OK,
            data = pagination,
            message = "Users returned"
        )

    }
}