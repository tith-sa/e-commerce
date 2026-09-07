package spring.ecommerce.handleException

class NotFoundException(message: String, val data: Any? = null) : RuntimeException(message)