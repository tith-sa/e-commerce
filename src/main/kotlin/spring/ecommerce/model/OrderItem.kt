package spring.ecommerce.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal


@Entity
@Table(name = "order_item")
data class OrderItem(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "order_id")
    var orderId: Long? = null,

    @Column(name = "product_id")
    var productId: Long? = null,

    @Column(name = "product_name")
    var productName: String? = null,

    @Column(name = "product_owner")
    var productOwner: String? = null,

    @Column(name = "product_Image", columnDefinition = "text")
    var productImage: String? = null,

    @Column(name = "quantity")
    var quantity: Int? = null,

    @Column(name = "product_unit_price", precision = 10, scale = 2)
    var unitPrice: BigDecimal? = null,

    @Column(name = "amount", precision = 10, scale = 2)
    var amount: BigDecimal? = null,
)
