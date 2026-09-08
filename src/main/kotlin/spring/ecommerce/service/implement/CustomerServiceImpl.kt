package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CustomerRequest
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchCustomerRequest
import spring.ecommerce.dto.request.UpdatedCustomerRequest
import spring.ecommerce.dto.response.CustomerResponse
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Customer
import spring.ecommerce.repository.CustomerRepository
import spring.ecommerce.repository.specification.customerSpecification
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

    override fun getAllCustomers(request: PaginationRequest): Response<PaginationResponse<CustomerResponse>> {
        val (page, size) = request

        val pageable = PageRequest.of(
            page -1,
            size,
        )
        val customers = customerRepository.findAll(pageable)

        val mapCustomer = customers.content.map{
            CustomerResponse(
                id = it.id,
                fullName = it.fullName,
                phoneNumber = it.phoneNumber,
                address = it.address,
                isDeleted = it.isDeleted,
            )
        }

        val pagination = PaginationResponse(
            PaginationResponse.ResponsePageMeta(
                page = page,
                pageSize = size,
                totalElements = customers.totalElements,
                totalPages = customers.totalPages,
            ),
            contents = mapCustomer

        )

        return Response(
            status = HttpStatus.OK,
            data = pagination,
            message = "customers retrieved"
        )
    }

    @Transactional
    override fun updateCustomer(customerId:Long, request: UpdatedCustomerRequest): Response<CustomerResponse> {
        val (fullName, phoneNumber, address) = request
        val customer = customerRepository.findById(customerId).orElseThrow {
            NotFoundException("Customer not found")
        }

        fullName?.let {
            customer.fullName = it
        }

        phoneNumber?.let {
            if (customerRepository.existsByPhoneNumber(phoneNumber)){
                throw BadRequestException("Phone number already exists")
            }
            customer.phoneNumber = it
        }

        address?.let {
            customer.address = it
        }

        customerRepository.save(customer)

        val response = CustomerResponse(
            id = customer.id,
            fullName = customer.fullName,
            phoneNumber = customer.phoneNumber,
            address = customer.address,
            isDeleted = customer.isDeleted,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Customer updated"
        )
    }

    override fun updatedIsCustomerDeleted(id: Long): Response<CustomerResponse> {
        val customer = customerRepository.findById(id).orElseThrow{
            NotFoundException("Customer not found")
        }

        customer.isDeleted = true

        customerRepository.save(customer)

        val response = CustomerResponse(
            id = customer.id,
            fullName = customer.fullName,
            phoneNumber = customer.phoneNumber,
            address = customer.address,
            isDeleted = customer.isDeleted,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "customer is deleted"
        )
    }

    override fun getCustomerById(id: Long): Response<CustomerResponse> {
        val customer = customerRepository.findById(id).orElseThrow{
            throw NotFoundException("Customer not found")
        }
        val response = CustomerResponse(
            id = customer.id,
            fullName = customer.fullName,
            phoneNumber = customer.phoneNumber,
            address = customer.address,
            isDeleted = customer.isDeleted,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Retrieve a customer"
        )
    }

    override fun searchCustomer(request: SearchCustomerRequest, requestPagination: PaginationRequest): Response<PaginationResponse<CustomerResponse>> {
        val (page, size) = requestPagination
        val pageable = PageRequest.of(
            page -1,
            size,
        )
        val specification = customerSpecification(request)
        val customers = customerRepository.findAll(specification, pageable)

        val mapCustomer = customers.content.map{
            CustomerResponse(
                id = it.id,
                fullName = it.fullName,
                phoneNumber = it.phoneNumber,
                address = it.address,
                isDeleted = it.isDeleted,
            )
        }
        val pagination = PaginationResponse(
            PaginationResponse.ResponsePageMeta(
                page = page,
                pageSize = size,
                totalElements = customers.totalElements,
                totalPages = customers.totalPages,
            ),
            contents = mapCustomer
        )
        return Response(
            status = HttpStatus.OK,
            data = pagination,
            message = "customers retrieved"
        )
    }
}