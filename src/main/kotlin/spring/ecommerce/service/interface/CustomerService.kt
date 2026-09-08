package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CustomerRequest
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchCustomerRequest
import spring.ecommerce.dto.request.UpdatedCustomerRequest
import spring.ecommerce.dto.response.CustomerResponse
import spring.ecommerce.dto.response.PaginationResponse

interface CustomerService {
    fun createCustomer(request: CustomerRequest): Response<CustomerResponse>
    fun getAllCustomers(request: PaginationRequest): Response<PaginationResponse<CustomerResponse>>
    fun updateCustomer(customerId: Long, request: UpdatedCustomerRequest): Response<CustomerResponse>
    fun updatedIsCustomerDeleted(id: Long): Response<CustomerResponse>
    fun getCustomerById(id: Long): Response<CustomerResponse>
    fun searchCustomer(request: SearchCustomerRequest, requestPagination: PaginationRequest):Response<PaginationResponse<CustomerResponse>>
}