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
                "/api/categories/create",
                "/api/categories/update/{id}",
                "/api/auth/logout",
                "/api/products/create",
                "/api/customers/**",
            )
            .excludePathPatterns(
                "/api/auth/login",
                "/api/products",
                "/api/categories",
                "/api/categories/search",
                "/swagger-ui/**",
                "/v3/api-docs/**"
            )
    }
}