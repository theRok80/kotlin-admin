package theRok.admin.com.admin.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.jwt")
data class JwtProperties(
    /** HS256용 최소 32바이트(256비트) UTF-8 문자열 */
    val secret: String = "",
    val accessTokenExpirationMinutes: Long = 60 * 24 * 30,
    val refreshTokenExpirationDays: Long = 7,
)
