package spring.ecommerce.handleException


class BadRequestException(message: String, val data: Any? = null) : RuntimeException(message)