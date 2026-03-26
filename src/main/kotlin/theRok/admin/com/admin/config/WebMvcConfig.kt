package theRok.admin.com.admin.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import theRok.admin.com.admin.common.interceptor.AuthIpThrottle

@Configuration
class WebMvcConfig(
    private val authIpThrottle: AuthIpThrottle,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authIpThrottle).addPathPatterns("/api/auth/**")
    }
}
