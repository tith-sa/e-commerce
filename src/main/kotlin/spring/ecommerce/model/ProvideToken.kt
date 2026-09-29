package spring.ecommerce.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import spring.ecommerce.config.AppConstraints
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "provide_token")
data class ProvideToken(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "access_token")
    var accessToken: String? = null,

    @Column(name = "refresh_token")
    var refreshToken: String? = null,

    @Column(name = "user_id")
    var userId: Long? = null,

    @JsonFormat(
        pattern = AppConstraints.DATETIME_PATTERN,
        timezone = AppConstraints.ZONE_ID,
        shape = JsonFormat.Shape.STRING
    )
    var accessExpiresAt: LocalDateTime? = null,

    @JsonFormat(
        pattern = AppConstraints.DATETIME_PATTERN,
        timezone = AppConstraints.ZONE_ID,
        shape = JsonFormat.Shape.STRING
    )
    var refreshExpiresAt: LocalDateTime? = null,

    @Column(name = "revoked")
    var revoked: Boolean = false,
)
