package spring.ecommerce.handleException

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import spring.ecommerce.dto.Response
import spring.ecommerce.util.buildError


@RestControllerAdvice
class GlobalException {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidateException(ex: MethodArgumentNotValidException): ResponseEntity<Response<Nothing>> {
        val errors = ex.bindingResult.fieldErrors.map {
            Response.Error(
                field = it.field,
                message = it.defaultMessage
            )
        }
        val response = Response(
            status = HttpStatus.BAD_REQUEST,
            data = null,
            message = "Validation failed",
            error = errors
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<Response<Any>> {
        return HttpStatus.BAD_REQUEST.buildError(ex.message, ex.data)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<Response<Any>> {
        return HttpStatus.NOT_FOUND.buildError(ex.message, ex.data)
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(ex: ForbiddenException): ResponseEntity<Response<Any>> {
        return HttpStatus.FORBIDDEN.buildError(ex.message, ex.data)
    }

    @ExceptionHandler(UnauthorizationException::class)
    fun handleAuthorizationException(ex: UnauthorizationException): ResponseEntity<Response<Any>> {
        return HttpStatus.UNAUTHORIZED.buildError(ex.message, ex.data)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleException(ex: RuntimeException): ResponseEntity<Response<Any>> {
        return HttpStatus.INTERNAL_SERVER_ERROR.buildError(ex.message)
    }
}