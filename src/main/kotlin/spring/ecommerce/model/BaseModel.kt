package spring.ecommerce.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import spring.ecommerce.config.AppConstraints
import java.time.LocalDateTime


@MappedSuperclass
abstract class BaseModel(
    @CreationTimestamp
    @JsonFormat(
        pattern = AppConstraints.DATETIME_PATTERN,
        timezone = AppConstraints.ZONE_ID,
        shape = JsonFormat.Shape.STRING
    )
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @JsonFormat(
        pattern = AppConstraints.DATETIME_PATTERN,
        timezone = AppConstraints.ZONE_ID,
        shape = JsonFormat.Shape.STRING
    )
    var updatedAt: LocalDateTime? = null,
)