package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchUserRequest
import spring.ecommerce.dto.request.UpdatedUserRequest
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

    override fun create(request: UserRequest): Response<Unit> {
        val (username, phoneNumber, password, address, isDeleted) = request
        username?.let {
            if(userRepository.existsByUsernameIgnoreCase(it)) {
                throw BadRequestException("Username already exists")
            }
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw BadRequestException("Phone number already exists")
        }

        val role = roleRepository.findById(2)
            .orElseThrow {
                NotFoundException("Role not found.")
            }

        val hashedPassword = passwordEncoder.encode(password)
        val user = User(
            username = username?.lowercase() ?: phoneNumber,
            phoneNumber = phoneNumber,
            address = address,
            roleId = role.id,
            password = hashedPassword,
            isDeleted = isDeleted
        )
        userRepository.save(user)

        return Response(
            status = HttpStatus.CREATED,
            data = null,
            message = "User created"
        )
    }


    override fun viewUser(id: Long): Response<UserResponse>{
        val user = userRepository.findByIdAndIsDeletedFalse(id).orElseThrow {
            NotFoundException("User not found.")
        }

        val response = UserResponse(
            id = user.id,
            username = user.username,
            phoneNumber = user.phoneNumber,
            address = user.address,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "User returned"
        )
    }


    @Transactional
    override fun updateUser(id: Long, request: UpdatedUserRequest): Response<Unit> {
        val (username, phoneNumber, password, address, roleId) = request

        val user = userRepository.findByIdAndIsDeletedFalse(id).orElseThrow{
            NotFoundException("User not found.")
        }

        username?.let {
            if(userRepository.existsByUsernameIgnoreCase(it)) {
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

        roleId?.let {
            val role = roleRepository.findById(it).orElseThrow {
                NotFoundException("Role not found.")
            }
            if (role.name.equals("ADMIN", ignoreCase = true)) {
                throw BadRequestException("Cannot assign ADMIN role.")
            }

            user.roleId = role.id
        }

        userRepository.save(user)


        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "User updated"
        )
    }



    override fun deletedUser(id: Long): Response<Unit> {
        val user = userRepository.findByIdAndIsDeletedFalse(id).orElseThrow {
            NotFoundException("User not found.")
        }

        user.isDeleted = true
        userRepository.save(user)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "User deleted"
        )
    }

    override fun listUsers(
        search: SearchUserRequest,
        requestPagination: PaginationRequest
    ): Response<PaginationResponse<UserResponse>> {
        val (page, size) = requestPagination
        val pageable = PageRequest.of(
            page - 1,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt"),

        )
        val specification = userSpecification(search)
        val users = userRepository.findAll(specification, pageable)

        val mapUser = users.content.map {
            UserResponse(
                id = it.id,
                username = it.username,
                phoneNumber = it.phoneNumber,
                address = it.address,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
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