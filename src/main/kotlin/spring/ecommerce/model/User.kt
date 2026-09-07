package spring.ecommerce.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", unique = true, length = 50)
    var username: String? = null,

    @Column(name = "phone_number", unique = true, length = 50)
    var phoneNumber: String? = null,

    @Column(name = "address", length = 255)
    var address: String? = null,

    @Column(name = "password", length = 255)
    var password: String? = null,

    @Column(name = "role_id")
    var roleId: Long? = null,

    @Column(name = "is_deleted")
    var isDeleted: Boolean? = null,

): BaseModel()
