package theRok.admin.com.admin.common.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import org.slf4j.MDC
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import theRok.admin.com.admin.common.constants.LogConstants
import theRok.admin.com.admin.common.constants.MaintenanceConstants
import theRok.admin.com.admin.config.MaintenanceProperties
import theRok.admin.com.admin.dto.ErrorResponseDto

private val log = KotlinLogging.logger {}

@Component
class MaintenanceFilter(
    private val maintenanceProperties: MaintenanceProperties,
    private val messageSource: MessageSource,
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {
    private val maintenanceDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val pathMatcher = AntPathMatcher()

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = pathForMatch(request)
        val rules = maintenanceProperties.pathRules

        if (rules.excludePathPatterns.any { pathMatcher.match(it, path) }) {
            return true
        }
        if (rules.includePathPatterns.isNotEmpty() &&
            rules.includePathPatterns.none { pathMatcher.match(it, path) }
        ) {
            return true
        }
        return false
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val startAt = LocalDateTime.parse(MaintenanceConstants.START_AT, maintenanceDateTimeFormatter)
        val endAt = LocalDateTime.parse(MaintenanceConstants.END_AT, maintenanceDateTimeFormatter)
        val now = LocalDateTime.now()
        if (now.isAfter(startAt) && now.isBefore(endAt)) {
            // 필터는 DispatcherServlet 앞에서 돌아가므로 @RestControllerAdvice가 잡지 못함 → 응답을 직접 씀
            val message =
                messageSource.getMessage("error.serverMaintenance", null, Locale.getDefault())
                    ?: "Service is under maintenance"
            response.status = HttpServletResponse.SC_SERVICE_UNAVAILABLE
            response.setContentType("application/json;charset=UTF-8")
            val body =
                ErrorResponseDto(
                    traceId = MDC.get(LogConstants.TRACE_ID) ?: "",
                    message = message,
                )
            objectMapper.writeValue(response.writer, body)
            log.info { message }
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun pathForMatch(request: HttpServletRequest): String {
        val uri = request.requestURI ?: "/"
        val contextPath = request.contextPath.orEmpty()
        if (contextPath.isNotEmpty() && uri.startsWith(contextPath)) {
            val relative = uri.substring(contextPath.length)
            return relative.ifEmpty { "/" }
        }
        return uri.ifEmpty { "/" }
    }
}
