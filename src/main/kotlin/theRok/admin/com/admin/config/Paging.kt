package theRok.admin.com.admin.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.PageableHandlerMethodArgumentResolver

@Configuration
@EnableSpringDataWebSupport(
    pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
class WebConfig
