package theRok.admin.com.admin.common.interceptor

import org.springframework.web.servlet.HandlerInterceptor
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import io.github.oshai.kotlinlogging.KotlinLogging
import theRok.admin.com.admin.service.RedisService
import theRok.admin.com.admin.common.constants.MAX_THROTTLE_COUNT
import theRok.admin.com.admin.common.constants.THROTTLE_TTL
import theRok.admin.com.admin.exception.TooManyRequestsException
import java.time.Duration

private val log = KotlinLogging.logger {}

/**
 * 로그인 시도 횟수를 제한하는 인터셉터
 *
 * @param redisService RedisService
 * @return Boolean
 * @throws TooManyRequestsException 로그인 시도 횟수가 최대 허용 횟수를 초과한 경우
 * @throws Exception 기타 예외 발생 시
 * @author theRok
 * @version 1.0
 * @since 2026-03-23
 */
@Component
class AuthIpThrottle(
    private val redisService: RedisService,
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val ip = request.getRemoteAddr().replace(":","")

        var redisIpKey = "auth:throttle:ip:$ip"
        var redisIpValue = redisService.getValue(redisIpKey)
        var ipCount = redisIpValue?.toInt() ?: 0

        if (ipCount >= MAX_THROTTLE_COUNT) {
            throw TooManyRequestsException("Too many requests. IP: $ip, Count: $ipCount")
        }

        if (redisIpValue == null) {
            redisService.setValue(redisIpKey, "1")
        } else {
            redisService.incrementValue(redisIpKey)
        }
        redisService.setExpire(redisIpKey, Duration.ofMinutes(THROTTLE_TTL))

        return true
    }
}
