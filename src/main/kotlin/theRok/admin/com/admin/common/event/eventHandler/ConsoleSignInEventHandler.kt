package theRok.admin.com.admin.common.event.eventHandler

import org.springframework.stereotype.Component
import org.springframework.context.event.EventListener
import theRok.admin.com.admin.common.event.ConsoleSignInEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import theRok.admin.com.admin.service.RedisService
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.Duration
import theRok.admin.com.admin.entity.ConsoleUserId

private val log = KotlinLogging.logger {}

data class SignInPayload(
    val consoleUserId: ConsoleUserId,
    val permissions: List<String>,
)

@Component
class ConsoleSignInEventHandler(
    private val redisService: RedisService,
    private val json: ObjectMapper,
) {
    fun getSignInKey(consoleUserId: ConsoleUserId, token: String): String {
        return "console:signIn:${consoleUserId}:${token}"
    }

    @EventListener
    fun handleConsoleSignInEvent(event: ConsoleSignInEvent) {
        val key = getSignInKey(event.consoleUserId, event.token)

        log.info { "key: $key" }
        log.info { "permissions: ${event.permissions}" }

        // 다른 토큰 삭제
        redisService.deleteValueByPrefix("console:signIn:${event.consoleUserId}")

        val payload = SignInPayload(consoleUserId = event.consoleUserId, permissions = event.permissions)
        redisService.setValue(key, json.writeValueAsString(payload))
        redisService.setExpire(key, Duration.ofDays(30))
    }
}
