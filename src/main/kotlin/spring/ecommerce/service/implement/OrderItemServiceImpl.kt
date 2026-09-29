package spring.ecommerce.service.implement

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ecommerce.dto.Response
import spring.ecommerce.dto.request.OrderItemRequest
import spring.ecommerce.dto.request.UpdatedOrderItemRequest
import spring.ecommerce.dto.response.OrderItemResponse
import spring.ecommerce.handleException.BadRequestException
import spring.ecommerce.handleException.NotFoundException
import spring.ecommerce.model.OrderItem
import spring.ecommerce.model.enum.OrderStatus
import spring.ecommerce.repository.OrderItemRepository
import spring.ecommerce.repository.OrderRepository
import spring.ecommerce.repository.ProductImageRepository
import spring.ecommerce.repository.ProductRepository
import spring.ecommerce.service.`interface`.OrderItemService
import java.math.BigDecimal
import kotlin.collections.map
import kotlin.plus


@Service
class OrderItemServiceImpl(
    private val orderItemRepository: OrderItemRepository,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
): OrderItemService {

    @Transactional
    override fun addOrderItem(
        orderId: Long,
        request: OrderItemRequest ,
    ): Response<Unit>{
        val (productId, quantity) = request

        // validate order is exist
        val order = orderRepository.findById(orderId).orElseThrow {
            NotFoundException("Order not found")
        }

        // validate only order status is pending can add new item
        if ( order.orderStatus != OrderStatus.PENDING.value ) {
            throw BadRequestException("Can not add new order item")
        }

        // validate product is exist?
        val item = productRepository.findById(productId).orElseThrow{
            NotFoundException("Product not found")
        }

        // find product image by product id and isPrimary true
        val itemId = item.id
            ?: throw NotFoundException("Item not found")
        val productImage = productImageRepository.findAllByProductId(itemId)
        val image = productImage
            .firstOrNull { it.displayOrder == 1 }

        val itemPrice = item.price ?: BigDecimal.ZERO

        // validate product quantity(check stock)
        item.quantity?.let {
            if (it < quantity) {
                throw BadRequestException("Item doesn't have enough stock")
            }
        }

        // update product quantity that's minus item quantity
        item.quantity = item.quantity?.minus(quantity)
        productRepository.save(item)

        // calculate amount
        val itemAmount = itemPrice * BigDecimal(quantity)

        // update and calculate subtotal (sum new item amount with the old subtotal)
        val subtotal = order.subtotal?.plus(itemAmount)

        // update new subtotal and grand total and save it to order table in db
        order.subtotal = subtotal
        order.grandTotal = subtotal
        orderRepository.save(order)

        // create order item
        val orderItem = OrderItem(
                orderId = orderId,
                productId = item.id,
                productName = item.name,
                productImage = image?.imageUrl,
                quantity = quantity,
                unitPrice = itemPrice,
                amount = itemAmount,
            )

        // save an orderItem to db
        orderItemRepository.save(orderItem)

        // return response
        return Response(
            status = HttpStatus.CREATED,
            data = null,
            message = "new order item created"
        )


    }

    @Transactional
    override fun updateOrderItem(id: Long, request: UpdatedOrderItemRequest): Response<OrderItemResponse> {
        val (quantity) = request

        // validate order item is exist
        val orderItem = orderItemRepository.findById(id).orElseThrow {
            NotFoundException("Order item not found")
        }

        // validate order is exist
        val orderId = orderItem.orderId ?: throw NotFoundException("Order not found")
        val order = orderRepository.findById(orderId).orElseThrow {
            NotFoundException("Order not found")
        }

        // can update while order pending
        if (order.orderStatus != OrderStatus.PENDING.value){
            throw BadRequestException("Update order not allowed")
        }

        // validate item is exist
        val itemId = orderItem.productId ?: throw NotFoundException("Item not found")
        val item = productRepository.findById(itemId).orElseThrow {
            NotFoundException("Item not found")
        }

        // check stock item quantity and update product stock
        var itemQuantity = item.quantity ?: 0
        orderItem.quantity?.let {itemQuantity += it }

        if (itemQuantity < quantity) {
            throw BadRequestException("Doesn't have enough stock")
        }

        val totalItemQuantity = itemQuantity - quantity
        item.quantity = totalItemQuantity
        productRepository.save(item)

        val itemPrice = item.price ?: BigDecimal.ZERO
        val itemAmount = itemPrice * BigDecimal(quantity)

        // update and calculate subtotal
        var subtotal = order.subtotal ?: BigDecimal.ZERO
        orderItem.amount?.let { subtotal -= it }
        subtotal += itemAmount

        // update subtotal and grand total and save it to order table in db
        order.subtotal = subtotal
        order.grandTotal = subtotal
        orderRepository.save(order)

        // save update order item quantity and also update amount
        orderItem.quantity = quantity
        orderItem.amount = itemAmount
        orderItemRepository.save(orderItem)

        val response = OrderItemResponse(
            id = orderItem.id,
            productId = orderItem.productId,
            productName = orderItem.productName,
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

        // validate order item is exist
        val orderItem = orderItemRepository.findById(id)
            .orElseThrow {
                NotFoundException("Order item not found")
            }

        // validate order is exist
        val orderId = orderItem.orderId
            ?: throw NotFoundException("Order not found")
        val order = orderRepository.findById(orderId)
            .orElseThrow {
                NotFoundException("Order not found")
            }

        // check condition only order status is pending that allowed to delete
        if (order.orderStatus != OrderStatus.PENDING.value) {
            throw BadRequestException("Delete order not allowed")
        }

        // validate item is exist
        val itemId = orderItem.productId
            ?: throw NotFoundException("Product not found")

        val item = productRepository.findById(itemId)
            .orElseThrow {
                NotFoundException("Product not found")
            }

        // Return quantity to product stock
        val itemQuantity = orderItem.quantity ?: 0
        item.quantity = item.quantity?.plus(itemQuantity)
        productRepository.save(item)

        // Subtract order item amount from old
        val subtotal = orderItem.amount?.let { order.subtotal?.minus(it) }

        // update subtotal and grand total and save it to order table in db
        order.subtotal = subtotal
        order.grandTotal = subtotal
        orderRepository.save(order)

        // Delete order item
        orderItemRepository.delete(orderItem)

        return Response(
            status = HttpStatus.OK,
            data = null,
            message = "Order item deleted"
        )
    }

    override fun viewOrderItem(id: Long): Response<OrderItemResponse> {
        val orderItem = orderItemRepository.findById(id).orElseThrow {
            NotFoundException("Order item not found")
        }

        val response = OrderItemResponse(
            id = orderItem.id,
            productId = orderItem.productId,
            productName = orderItem.productName,
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
}


