package theRok.admin.com.admin.security

import org.springframework.stereotype.Component
import io.github.oshai.kotlinlogging.KotlinLogging
import theRok.admin.com.admin.common.constants.PermissionConstants
import theRok.admin.com.admin.entity.ConsoleUserId

private val log = KotlinLogging.logger {}

@Component
class SetUserPermissionProvider : UserPermissionProvider {
    override fun permissionsFor(userId: ConsoleUserId): List<String> {
        log.info { "userId: $userId" }

        return when (userId) {
            "theRok" -> listOf("all:write")
            else -> emptyList()
        }
    }
}
