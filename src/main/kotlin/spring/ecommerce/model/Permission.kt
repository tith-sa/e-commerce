package spring.ecommerce.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "permissions")
data class Permission(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "parent_id")
    var parentId: Long? = null,

    @Column(name = "code", unique = true)
    var code: String? = null,

    @Column(name = "permission_name", length = 50)
    var name: String? = null,
) : BaseModel()
