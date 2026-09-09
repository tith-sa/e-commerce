package spring.ecommerce.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import spring.ecommerce.model.enum.OrderStatus
import spring.ecommerce.model.enum.PaymentStatus
import java.math.BigDecimal

@Entity
@Table(name = "order")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "customer_id")
    var customerId: Long? = null,

    @Column(name = "customer_full_name", length = 50)
    var customerFullName: String? = null,

    @Column(name = "customer_phone_number", length = 20)
    var customerPhoneNumber: String? = null,

    @Column(name = "customer_address", length = 255)
    var customerAddress: String? = null,

    @Column(name = "total_Amount", precision = 10, scale = 2)
    var totalAmount: BigDecimal? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", length = 10)
    var orderStatus: OrderStatus? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 10)
    var paymentStatus: PaymentStatus? = null,


): BaseModel()
