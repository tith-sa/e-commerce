package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.response.OrderResponse
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Order
import spring.ecommerce.model.enum.OrderStatus
import spring.ecommerce.model.enum.PaymentStatus
import spring.ecommerce.repository.CustomerRepository
import spring.ecommerce.repository.OrderRepository
import spring.ecommerce.service.`interface`.OrderItemService
import spring.ecommerce.service.`interface`.OrderService
import java.math.BigDecimal


@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderItemService: OrderItemService,
    private val customerRepository: CustomerRepository,
) : OrderService {

    @Transactional
    override fun createOrder(customerId: Long): Response<OrderResponse>{
        val customer = customerRepository.findByIdAndIsDeletedFalse(customerId).orElseThrow{
            NotFoundException("Customer not found")
        }

        val order = Order(
            customerId = customer.id,
            customerFullName = customer.fullName,
            customerPhoneNumber = customer.phoneNumber,
            customerAddress = customer.address,
            totalAmount = BigDecimal.ZERO.setScale(2),
            orderStatus = OrderStatus.PENDING,
            paymentStatus = PaymentStatus.PENDING,
        )

        orderRepository.save(order)

        val response = OrderResponse(
            id = order.id,
            customerId = order.customerId,
            customerFullName = order.customerFullName,
            customerPhoneNumber = order.customerPhoneNumber,
            customerAddress = order.customerAddress,
            totalAmount = order.totalAmount,
            orderStatus = order.orderStatus,
            paymentStatus = order.paymentStatus,
            createdAt = order.createdAt,
            updatedAt = order.updatedAt
        )

        return Response(
            status = HttpStatus.CREATED,
            data = response,
            message = "Order created"
        )
    }

    override fun getOrderById(id: Long): Response<OrderResponse> {
        val order = orderRepository.findById(id).orElseThrow {
            NotFoundException("Order not found")
        }

        val orderItems = orderItemService.getAllOrderItemsByOrder(id)

        val response = OrderResponse(
            id = order.id,
            customerId = order.customerId,
            customerFullName = order.customerFullName,
            customerPhoneNumber = order.customerPhoneNumber,
            customerAddress = order.customerAddress,
            totalAmount = order.totalAmount,
            orderStatus = order.orderStatus,
            orderItems = orderItems,
            paymentStatus = order.paymentStatus,
            createdAt = order.createdAt,
            updatedAt = order.updatedAt
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Received order"
        )
    }
}