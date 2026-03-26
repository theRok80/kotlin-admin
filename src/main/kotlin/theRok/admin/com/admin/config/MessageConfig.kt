package theRok.admin.com.admin.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class MessageConfig {
    @Bean
    fun messageSource(): MessageSource {
        val source = ReloadableResourceBundleMessageSource()
        source.setBasenames("classpath:messages")
        source.setDefaultEncoding("UTF-8")
        source.setFallbackToSystemLocale(false)
        return source
    }
}
