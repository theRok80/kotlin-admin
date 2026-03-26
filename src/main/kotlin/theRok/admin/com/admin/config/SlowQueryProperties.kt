package theRok.admin.com.admin.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.slow-query")
data class SlowQueryProperties(
    val enabled: Boolean = false,
    val thresholdMs: Long = 500,
    val forceExplain: Boolean = false,
)
