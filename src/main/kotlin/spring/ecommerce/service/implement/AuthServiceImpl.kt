package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.LoginRequest
import spring.ecommerce.dto.response.LoginResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.repository.RoleRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.security.JwtUtil
import spring.ecommerce.service.`interface`.AuthService

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val roleRepository: RoleRepository
) : AuthService {

    override fun login(request: LoginRequest): Response<LoginResponse> {
        val (username, password) = request

        val user = userRepository.findByUsername(username).orElseThrow {
            throw NotFoundException("User not found")
        }

        if (!passwordEncoder.matches(password, user.password)){
            throw BadRequestException("Incorrect password")
        }

        val role = user.roleId
            ?: throw NotFoundException("Role name not found")

        val roleName = roleRepository.findById(role).orElseThrow{
            throw NotFoundException("Role not found")
        }

        val token = jwtUtil.generateToken(user.id!!, roleName.name!!)

        val response = LoginResponse(
            token = token,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "user logged in"
        )

    }

    override fun logout(userId : Long): Response<String> {
        return Response(
            status = HttpStatus.OK,
            data = "Logout successful",
            message = "User logged out"
        )
    }
}