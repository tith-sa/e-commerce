package spring.ecommerce.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import spring.ecommerce.security.JwtInterceptor

@Configuration
class WebConfig(
    private val jwtInterceptor: JwtInterceptor
): WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns(
                "/api/roles/**",
                "/api/users/**",
                "/api/categories/**",
                "/api/products/**",
                "/api/customers/**",
                "/api/orders/**",
                "/api/order-items/**",
                "/api/permissions/**",
            )
            .excludePathPatterns(
                "/api/auth/login",
                "/api/auth/refresh",
                "/api/auth/logout",
                "/swagger-ui/**",
                "/v3/api-docs/**"
            )
    }
}