package theRok.admin.com.admin.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import theRok.admin.com.admin.common.event.eventHandler.SignInPayload
import theRok.admin.com.admin.service.RedisService

private val log = KotlinLogging.logger {}

class JwtAuthenticationFilter(
    private val redisService: RedisService,
    private val json: ObjectMapper,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean = isPublicPath(pathWithinApp(request))

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header.isNullOrBlank() || !header.startsWith(BEARER_PREFIX)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            return
        }
        val token = header.substring(BEARER_PREFIX.length).trim()
        if (token.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            return
        }

        // Redis에서 로그인 시 저장된 세션을 조회
        // 세션이 없으면 로그아웃·만료로 간주하여 즉시 인증 거부
        val storedValue = redisService.getConsoleSignInToken(token)
        if (storedValue == null) {
            log.warn { "Redis session not found for token=$token" }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            return
        }

        try {
            val payload = json.readValue(storedValue, SignInPayload::class.java)
            val authorities = payload.permissions.map { SimpleGrantedAuthority(it) }

            val authentication = UsernamePasswordAuthenticationToken(
                payload.consoleUserId,
                null,
                authorities,
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            log.error(e) { "Failed to parse SignInPayload from Redis" }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun pathWithinApp(request: HttpServletRequest): String {
        val uri = request.requestURI ?: "/"
        val contextPath = request.contextPath.orEmpty()
        if (contextPath.isNotEmpty() && uri.startsWith(contextPath)) {
            val relative = uri.substring(contextPath.length)
            return relative.ifEmpty { "/" }
        }
        return uri.ifEmpty { "/" }
    }

    private fun isPublicPath(path: String): Boolean =
        path.startsWith("/api/auth") ||
            path.startsWith("/health") ||
            path.startsWith("/error")

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }
}
