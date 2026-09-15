package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderItemRequest
import spring.ecommerce.dto.response.OrderItemResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.OrderItem
import spring.ecommerce.model.enum.OrderStatus
import spring.ecommerce.repository.OrderItemRepository
import spring.ecommerce.repository.OrderRepository
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.repository.ProductRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.service.`interface`.OrderItemService
import java.math.BigDecimal
import kotlin.collections.map


@Service
class OrderItemServiceImpl(
    private val orderItemRepository: OrderItemRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val usrRepository: UserRepository,
    private val productImageRepository: ProductImageRepository,
): OrderItemService {

    @Transactional
    override fun createOrderItem(orderId: Long, productId: Long, request: OrderItemRequest): Response<OrderItemResponse> {
        val (quantity) = request
        val order = orderRepository.findById(orderId).orElseThrow {
            NotFoundException("Order not found")
        }

        if (order.orderStatus != OrderStatus.PENDING){
            throw BadRequestException("Add order not allowed")
        }

        val product = productRepository.findById(productId).orElseThrow {
            NotFoundException("Product not found")
        }

        val productName = product.name
            ?: throw NotFoundException("Product not found")

        val userId = product.createdBy ?: throw BadRequestException("User not found")
        val productOwner = usrRepository.findById(userId).orElseThrow{
            NotFoundException("User not found")
        }

        val productImage = productImageRepository.findAllByProductIdAndIsPrimary( productId, true)

        val productQuantity = product.quantity
            ?: 0
        if ( quantity >  productQuantity ) {
            throw BadRequestException("Doesn't have enough stock")
        }


        val productPrice = product.price
            ?: throw BadRequestException("Product price isn't set")
        val amount = productPrice.multiply(BigDecimal(quantity))

        val orderItem = OrderItem(
            orderId = orderId,
            productId = productId,
            quantity = quantity,
            amount = amount,
            unitPrice = productPrice,
            productImage = productImage.imageUrl,
            productName = productName,
            productOwner = productOwner.username
        )

        orderItemRepository.save(orderItem)

        val totalProductQuantity = productQuantity - quantity

        product.quantity = totalProductQuantity

        productRepository.save(product)

        val orderItems = orderItemRepository.findAllByOrderId(orderId)

        val totalAmount = orderItems.fold(BigDecimal.ZERO) { total, item ->
            total + (item.amount ?: BigDecimal.ZERO)
        }

        order.totalAmount = totalAmount

        orderRepository.save(order)

        val response = OrderItemResponse(
            id = orderItem.id,
            productName = orderItem.productName,
            productOwner = orderItem.productOwner,
            productImage = orderItem.productImage,
            quantity = orderItem.quantity,
            unitPrice = orderItem.unitPrice,
            amount = orderItem.amount,
        )

        return Response(
            status = HttpStatus.CREATED,
            data = response,
            message = "order Item created"
        )
    }

    override fun getAllOrderItemsByOrder(orderId: Long): List<OrderItemResponse> {
        val orderItems = orderItemRepository
            .findAllByOrderId(orderId)
            .map {
                OrderItemResponse(
                    id = it.id,
                    productName = it.productName,
                    productOwner = it.productOwner,
                    productImage = it.productImage,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    amount = it.amount
                )
            }
        return orderItems
    }

    @Transactional
    override fun updateOrderItem(id: Long, request: OrderItemRequest): Response<OrderItemResponse> {
        val (quantity) = request
        val orderItem = orderItemRepository.findById(id).orElseThrow {
            NotFoundException("Order item not found")
        }

        val orderId = orderItem.orderId ?: throw NotFoundException("Order not found")
        val order = orderRepository.findById(orderId).orElseThrow {
            NotFoundException("Order not found")
        }

        if (order.orderStatus != OrderStatus.PENDING){
            throw BadRequestException("Update order not allowed")
        }

        val productId = orderItem.productId ?: throw NotFoundException("Product not found")
        val product = productRepository.findById(productId).orElseThrow {
            NotFoundException("Product not found")
        }

        val productQuantity = product.quantity
            ?: 0
        if (productQuantity < quantity) {
            throw BadRequestException("Doesn't have enough stock")
        }

        orderItem.quantity = quantity
        orderItem.amount = orderItem.unitPrice?.times(BigDecimal(quantity))

        orderItemRepository.save(orderItem)

        val totalProductQuantity = productQuantity - quantity

        product.quantity = totalProductQuantity

        productRepository.save(product)

        val orderItems = orderItemRepository.findAllByOrderId(orderId)

        val totalAmount = orderItems.fold(BigDecimal.ZERO) { total, item ->
            total + (item.amount ?: BigDecimal.ZERO)
        }

        order.totalAmount = totalAmount

        orderRepository.save(order)


        val response = OrderItemResponse(
            id = orderItem.id,
            productName = orderItem.productName,
            productOwner = orderItem.productOwner,
            productImage = orderItem.productImage,
            quantity = quantity,
            unitPrice = orderItem.unitPrice,
            amount = orderItem.amount,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "order Item updated"
        )
    }

    @Transactional
    override fun deleteOrderItemById(id: Long): Response<Unit> {

        val orderItem = orderItemRepository.findById(id)
            .orElseThrow {
                NotFoundException("Order item not found")
            }

        val orderId = orderItem.orderId
            ?: throw NotFoundException("Order not found")

        val order = orderRepository.findById(orderId)
            .orElseThrow {
                NotFoundException("Order not found")
            }

        if (order.orderStatus != OrderStatus.PENDING) {
            throw BadRequestException("Delete order not allowed")
        }

        // Find product
        val productId = orderItem.productId
            ?: throw NotFoundException("Product not found")

        val product = productRepository.findById(productId)
            .orElseThrow {
                NotFoundException("Product not found")
            }

        // Return quantity to product stock
        val orderItemQuantity = orderItem.quantity
            ?: 0
        product.quantity = product.quantity?.plus(orderItemQuantity)
        productRepository.save(product)

        // Subtract order item amount from order total
        order.totalAmount = (order.totalAmount ?: BigDecimal.ZERO) -
                    (orderItem.amount ?: BigDecimal.ZERO)

        orderRepository.save(order)

        // Delete order item
        orderItemRepository.delete(orderItem)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Order item deleted"
        )
    }

    override fun getOrderItemById(id: Long): Response<OrderItemResponse> {
        val orderItem = orderItemRepository.findById(id).orElseThrow {
            NotFoundException("Order item not found")
        }

        val response = OrderItemResponse(
            id = orderItem.id,
            productName = orderItem.productName,
            productOwner = orderItem.productOwner,
            productImage = orderItem.productImage,
            quantity = orderItem.quantity,
            unitPrice = orderItem.unitPrice,
            amount = orderItem.amount,
        )

        return Response(
            status = HttpStatus.OK,
            data = response,
            message = "Retrieved a order item"
        )
    }

    override fun getAllOrderItemByProductId(productId: Long): Response<List<OrderItemResponse>> {
        val orderItems = orderItemRepository
            .findAllByProductId(productId)
            .map {
            OrderItemResponse(
                id = it.id,
                productName = it.productName,
                productOwner = it.productOwner,
                productImage = it.productImage,
                quantity = it.quantity,
                unitPrice = it.unitPrice,
                amount = it.amount
            )
        }

        return Response(
            status = HttpStatus.OK,
            data = orderItems,
            message = "Order item retrieved"
        )
    }
}

