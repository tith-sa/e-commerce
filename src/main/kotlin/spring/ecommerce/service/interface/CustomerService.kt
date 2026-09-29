package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CustomerRequest
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchCustomerRequest
import spring.ecommerce.dto.request.UpdatedCustomerRequest
import spring.ecommerce.dto.response.CustomerResponse
import spring.ecommerce.dto.response.PaginationResponse

interface CustomerService {
    fun createCustomer(request: CustomerRequest): Response<Unit>
    fun listCustomers(search: SearchCustomerRequest,request: PaginationRequest): Response<PaginationResponse<CustomerResponse>>
    fun updateCustomer(customerId: Long, request: UpdatedCustomerRequest): Response<Unit>
    fun deletedCustomer(id: Long): Response<Unit>
    fun viewCustomer(id: Long): Response<CustomerResponse>
}