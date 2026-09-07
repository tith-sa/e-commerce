package spring.ecommerce.handleException

class ForbiddenException (message: String, val data: Any? = null) : RuntimeException(message)