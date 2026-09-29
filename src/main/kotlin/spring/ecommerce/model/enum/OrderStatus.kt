package spring.ecommerce.model.enum

enum class OrderStatus(val value: Int) {
    PENDING(1),
    PROCESSING(2),
    COMPLETED(3),
    CANCELLED(4)
}