package spring.ecommerce.service.`interface`

import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.CustomerRequest
import spring.ecommerce.dto.response.CustomerResponse

interface CustomerService {
    fun createCustomer(request: CustomerRequest): Response<CustomerResponse>
}