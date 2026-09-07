package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CustomerRequest
import spring.ecommerce.dto.response.CustomerResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.model.Customer
import spring.ecommerce.repository.CustomerRepository
import spring.ecommerce.service.`interface`.CustomerService

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
): CustomerService {

    override fun createCustomer(request: CustomerRequest): Response<CustomerResponse> {
        val (fullName, phoneNumber, address, isDeleted ) = request


        if (customerRepository.existsByPhoneNumber(phoneNumber)){
            throw BadRequestException("Phone number already exists")
        }

        val customer = Customer(
            fullName = fullName ?: phoneNumber,
            phoneNumber = phoneNumber,
            address = address,
            isDeleted = isDeleted

        )

        customerRepository.save(customer)

        val response = CustomerResponse(
            id = customer.id,
            fullName = customer.fullName,
            phoneNumber = customer.phoneNumber,
            address = customer.address,
            isDeleted = customer.isDeleted,
        )

        return Response(
            status = HttpStatus.CREATED,
            data = response,
            message = "customer created"
        )
    }
}