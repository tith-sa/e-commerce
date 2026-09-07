package spring.ecommerce.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "product")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "product_name", length = 255 )
    var name: String? = null,

    @Column(name = "product_price", precision = 10, scale = 2)
    var price: BigDecimal? = null,

    @Column(name = "description", columnDefinition = "text")
    var description: String? = null,

    @Column(name = "quantity")
    var quantity: Int? = null,

    @Column(name = "category_id")
    var categoryId: Long? = null,

    @Column(name = "created_by")
    var createdBy: Long? = null,


): BaseModel()
