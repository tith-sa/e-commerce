package spring.ecommerce.dto

import org.springframework.http.HttpStatus
import spring.ecommerce.config.AppConstraints
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Response<T> (
    val status: HttpStatus,
    val data: T?,
    val error: List<Error>? = emptyList(),
    val message: String?,
    val timestamp: String = DateTimeFormatter
        .ofPattern(AppConstraints.DATETIME_PATTERN)
        .withZone(ZoneId.of(AppConstraints.LOCAL_TZ))
        .format(Instant.now())
){
    data class Error(
        val field: String,
        val message: String?
    )
}
