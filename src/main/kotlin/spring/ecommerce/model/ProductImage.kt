package spring.ecommerce.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product_image")
data class ProductImage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "imageUrl", columnDefinition = "text")
    var imageUrl: String? = null,

    @Column(name = "display_order")
    var displayOrder: Int? = null,

    @Column(name = "is_primary")
    var isPrimary: Boolean? = null,

    @Column(name = "product_id")
    var productId: Long? = null,
)