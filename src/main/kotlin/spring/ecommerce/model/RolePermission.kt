package spring.ecommerce.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "role_permissions",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["permission_id", "role_id"]
        )
    ]
)
data class RolePermission(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "permission_id")
    var permissionId : Long? = null,

    @Column(name = "role_id")
    var roleId : Long? = null,

    @Column(name = "sort_order")
    var sortOrder : Int? = null,

    )
