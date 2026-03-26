package theRok.admin.com.admin.common.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import theRok.admin.com.admin.common.constants.LogConstants
import java.util.UUID

private val log = KotlinLogging.logger {}

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestLoggingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val traceId =
            request.getHeader(LogConstants.TRACE_ID_HEADER) ?: UUID.randomUUID().toString()

        try {
            response.setHeader(LogConstants.TRACE_ID_HEADER, traceId)
            MDC.put(LogConstants.TRACE_ID, traceId)

            filterChain.doFilter(request, response)
        } finally {
            MDC.remove(LogConstants.TRACE_ID)
        }
    }
}
