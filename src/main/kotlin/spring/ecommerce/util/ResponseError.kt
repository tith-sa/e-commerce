package spring.ecommerce.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spring.ecommerce.dto.Response

fun HttpStatus.buildError(
    message: String?,
    data: Any? = null
): ResponseEntity<Response<Any>> {
    val body = Response(
        status = this,
        data = data,
        message = message
    )
    return ResponseEntity
        .status(this)
        .body(body)
}