package spring.ecommerce.service.implement

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderRequest
import spring.ecommerce.dto.request.PaginationRequest
import spring.ecommerce.dto.request.SearchOrderRequest
import spring.ecommerce.dto.request.UpdatedOrderStatusRequest
import spring.ecommerce.dto.response.OrderItemResponse
import spring.ecommerce.dto.response.OrderResponse
import spring.ecommerce.dto.response.PaginationResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.Order
import spring.ecommerce.model.OrderItem
import spring.ecommerce.model.enum.OrderStatus
import spring.ecommerce.model.enum.PaymentStatus
import spring.ecommerce.repository.CustomerRepository
import spring.ecommerce.repository.OrderItemRepository
import spring.ecommerce.repository.OrderRepository
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.repository.ProductRepository
import spring.ecommerce.repository.specification.orderSpecification
import spring.ecommerce.service.`interface`.OrderService
import java.math.BigDecimal
import kotlin.collections.map
import kotlin.plus


@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val orderItemRepository: OrderItemRepository,
) : OrderService {

    @Transactional
    override fun createOrder(request: OrderRequest): Response<Unit>{
        val (customerId, remark, orderItems) = request // destructuring declaration
        // validate customer is exist?
        val customer = customerRepository.findByIdAndIsDeletedFalse(customerId).orElseThrow {
            NotFoundException("Customer not found")
        }

        // validate product is exist?
        val setProductIds = orderItems.map { it.productId }.toSet()
        val products = productRepository.findByIdIn(setProductIds)
        if (products.size != setProductIds.size) {
            throw NotFoundException("Product not found")
        }
        val productImages = productImageRepository.findAllByProductIdIn(setProductIds)

        // declare mutable list to add orderItem
        val saleOrderItemsPayload = mutableListOf<OrderItem>()

        var subtotal = BigDecimal.ZERO

        for (orderItem in orderItems ) {

            val item = products.find{ it.id == orderItem.productId }
                ?: throw NotFoundException("Product not found")

            val productImage = productImages
                .firstOrNull { it.displayOrder == 1 }

            val itemPrice = item.price ?: BigDecimal.ZERO
            val itemQuantity = orderItem.quantity

            // validate product quantity(check stock)
            item.quantity?.let {
                if (it < itemQuantity) {
                    throw BadRequestException("Item doesn't have enough stock")
                }
            }

            // update product quantity that's minus item quantity
            item.quantity = item.quantity?.minus(itemQuantity)
            productRepository.save(item)

            // calculate amount
            val itemAmount = itemPrice * BigDecimal(itemQuantity)

            // update and calculate subtotal (sum all item amount)
            subtotal += itemAmount

            // add each order item to saleOrderItemsPayload mutable list
            saleOrderItemsPayload.add(
                // create order item
                OrderItem(
                    productId = item.id,
                    productName = item.name,
                    productImage = productImage?.imageUrl,
                    quantity = orderItem.quantity,
                    unitPrice = itemPrice,
                    amount = itemAmount,
                )
            )

        }

        val grandTotal = subtotal

        // create order
        val order = Order(
            customerId = customer.id,
            remark = remark,
            customerFullName = customer.fullName,
            customerPhoneNumber = customer.phoneNumber,
            customerAddress = customer.address,
            subtotal = subtotal,
            grandTotal = grandTotal,
            orderStatus = OrderStatus.PENDING.value,
            paymentStatus = PaymentStatus.PENDING.value,
        )

        // save order to db
        orderRepository.save(order)

        // update orderId for each order item
        for (item in saleOrderItemsPayload) {
            item.orderId = order.id
        }

        // save all order item to db
        orderItemRepository.saveAll(saleOrderItemsPayload)

        // return response
        return Response(
            status = HttpStatus.CREATED,
            data = null,
            message = "Order created"
        )
    }

    override fun viewOrder(id: Long): Response<OrderResponse> {
        val order = orderRepository.findById(id).orElseThrow {
            NotFoundException("Order not found")
        }

        val orderItems = orderItemRepository
            .findAllByOrderId(id)
            .map {
                OrderItemResponse(
                    id = it.id,
                    productId = it.productId,
                    productName = it.productName,
                    productImage = it.productImage,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    amount = it.amount
                )
            }

        val response = OrderResponse(
            id = order.id,
            customerId = order.customerId,
            customerFullName = order.customerFullName,
            customerPhoneNumber = order.customerPhoneNumber,
            customerAddress = order.customerAddress,
            remark = order.remark,
            subtotal = order.subtotal,
            grandTotal = order.grandTotal,
            orderItems = orderItems,
            orderStatus = order.orderStatus,
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


    // task : query list orders by product id
    // order -> orderItem -> orderItem.productId
    override fun listOrders(search: SearchOrderRequest, request: PaginationRequest): Response<PaginationResponse<OrderResponse>> {
        val (page, size) = request

        // Define page number (0-indexed) and page size for database pagination
        val pageable = PageRequest.of(
            page - 1,
            size,
        )

        val specification = orderSpecification(search)
        val orders = orderRepository.findAll(specification, pageable)


        val orderId = orders.content.map {it.id}
        val orderItems = orderItemRepository.findAllByOrderIdIn(orderId)


        // map each order response
        val mapOrder = orders.content.map{ order ->

            val orderItem = orderItems.filter{ it.orderId == order.id  }

            val mapResponseOrderItem = orderItem.map{
                OrderItemResponse(
                    id = it.id,
                    productId = it.productId,
                    productName = it.productName,
                    productImage = it.productImage,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    amount = it.amount
                )
            }

            // order response
            OrderResponse(
                id = order.id,
                customerId =order.customerId,
                customerFullName = order.customerFullName,
                customerPhoneNumber = order.customerPhoneNumber,
                customerAddress = order.customerAddress,
                remark = order.remark,
                subtotal = order.subtotal,
                grandTotal = order.grandTotal,
                orderItems = mapResponseOrderItem,
                orderStatus = order.orderStatus,
                paymentStatus = order.paymentStatus,
                createdAt = order.createdAt,
                updatedAt = order.updatedAt
            )

        }

        // pagination response
        val pagination = PaginationResponse(
            meta = PaginationResponse.ResponsePageMeta(
                page = page,
                pageSize = size,
                totalElements = orders.totalElements,
                totalPages = orders.totalPages,
            ),
            contents = mapOrder,
        )

        return Response(
            status = HttpStatus.OK,
            data = pagination,
            message = "List order"
        )
    }

    override fun updateOrderStatus(id: Long, request: UpdatedOrderStatusRequest): Response<Unit> {
        val (status) = request
        // validate order is exist
        val order = orderRepository.findById(id).orElseThrow {
            NotFoundException("Order not found")
        }

        // validate status is exist
        val newOrderStatus = OrderStatus.entries.find { it.value == status }
            ?: throw NotFoundException("Order not found")

        // validate if update to the same status
        val currentOrderStatus = OrderStatus.entries.find { it.value == order.orderStatus }
            ?: throw BadRequestException("Cannot update order status")

        // Define allowed status transitions
        val allowedStatuses = when (currentOrderStatus) {

            OrderStatus.PENDING -> setOf(
                OrderStatus.PROCESSING,
                OrderStatus.CANCELLED
            )

            OrderStatus.PROCESSING -> setOf(
                OrderStatus.COMPLETED
            )

            OrderStatus.CANCELLED -> emptySet()

            OrderStatus.COMPLETED -> emptySet()
        }

        // Validate transition
        if (newOrderStatus !in allowedStatuses) {
            throw BadRequestException(
                "Can not update order from $currentOrderStatus to $newOrderStatus"
            )
        }


        order.orderStatus = status
        orderRepository.save(order)

        return Response(
            status = HttpStatus.CREATED,
            data = null,
            message = "Order updated"
        )
    }
}