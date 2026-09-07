package spring.ecommerce.handleException

class UnauthorizationException (message: String, val data: Any? = null) : RuntimeException(message)