package theRok.admin.com.admin.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.maintenance")
data class MaintenanceProperties(
    val pathRules: PathRules = PathRules(),
) {
    data class PathRules(
        val includePathPatterns: List<String> = emptyList(),
        val excludePathPatterns: List<String> = emptyList(),
    )
}
