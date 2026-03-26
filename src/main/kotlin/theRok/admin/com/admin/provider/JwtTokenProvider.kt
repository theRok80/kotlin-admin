package theRok.admin.com.admin.provider

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import theRok.admin.com.admin.config.JwtProperties
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val props: JwtProperties,
) {
    private val key: SecretKey by lazy {
        val bytes = props.secret.toByteArray(StandardCharsets.UTF_8)
        require(bytes.size >= 32) { "app.jwt.secret must be at least 32 bytes (UTF-8)" }
        Keys.hmacShaKeyFor(bytes)
    }

    fun createAccessToken(
        subject: String,
        permissions: List<String>,
    ): String {
        val now = Instant.now()
        val exp = now.plus(props.accessTokenExpirationMinutes, ChronoUnit.MINUTES)
        return Jwts
            .builder()
            .subject(subject)
            .claim(CLAIM_TYP, "access")
            .claim(CLAIM_PERMS, permissions)
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .signWith(key)
            .compact()
    }

    fun createRefreshToken(subject: String): String {
        val now = Instant.now()
        val exp = now.plus(props.refreshTokenExpirationDays, ChronoUnit.DAYS)
        return Jwts
            .builder()
            .subject(subject)
            .claim(CLAIM_TYP, "refresh")
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .signWith(key)
            .compact()
    }

    fun parseAccessToken(token: String): ParsedAccessToken {
        val claims =
            Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        if (claims[CLAIM_TYP] != "access") {
            throw JwtException("Not an access token")
        }
        val subject = claims.subject ?: throw JwtException("Missing subject")
        return ParsedAccessToken(subject, extractPermissions(claims[CLAIM_PERMS]))
    }

    /** 하위 호환·단순 검증용 */
    fun parseAccessTokenSubject(token: String): String = parseAccessToken(token).subject

    private fun extractPermissions(raw: Any?): List<String> {
        if (raw == null) {
            return emptyList()
        }
        if (raw is Collection<*>) {
            return raw.mapNotNull { it?.toString()?.trim()?.takeIf { s -> s.isNotEmpty() } }
        }
        if (raw is Array<*>) {
            return raw.mapNotNull { it?.toString()?.trim()?.takeIf { s -> s.isNotEmpty() } }
        }
        return emptyList()
    }

    companion object {
        private const val CLAIM_TYP = "typ"
        const val CLAIM_PERMS = "perms"
    }
}
