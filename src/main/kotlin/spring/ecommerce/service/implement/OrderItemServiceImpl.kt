package spring.ecommerce.service.implement

import org.springframework.stereotype.Service
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderItemRequest
import spring.ecommerce.dto.response.OrderItemResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.OrderItem
import spring.ecommerce.repository.OrderItemRepository
import spring.ecommerce.repository.OrderRepository
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.repository.ProductRepository
import spring.ecommerce.repository.UserRepository
import spring.ecommerce.service.`interface`.OrderItemService
import java.math.BigDecimal


@Service
class OrderItemServiceImpl(
    private val orderItemRepository: OrderItemRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val usrRepository: UserRepository,
    private val productImageRepository: ProductImageRepository
): OrderItemService {

    override fun createOrderItem(orderId: Long, productId: Long, request: OrderItemRequest): OrderItemResponse {
        val (quantity) = request
        orderRepository.findById(orderId).orElseThrow {
            NotFoundException("Order not found")
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
            ?: throw BadRequestException("Product quantity is missing")
        if ( quantity < productQuantity ) {
            throw BadRequestException("Quantity of product isn't enough")
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

        return OrderItemResponse(
            id = orderItem.id,
            productName = orderItem.productName,
            productOwner = orderItem.productOwner,
            productImage = orderItem.productImage,
            quantity = orderItem.quantity,
            unitPrice = orderItem.unitPrice,
            amount = orderItem.amount,
        )
    }
}

