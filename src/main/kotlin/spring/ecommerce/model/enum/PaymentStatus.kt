package spring.ecommerce.model.enum

enum class PaymentStatus (val value: Int) {
    PENDING(1),
    PAID(2),
    FAILED(3),
}