package theRok.admin.com.admin.common.util

import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger {}

@Component("auth")
class AuthorityUtil {
    fun hasAny(auth: Authentication, vararg permissions: String): Boolean {
        log.info { "auth: $auth" }
        log.info { "permissions: $permissions" }

        // return true

        val authorities = auth.authorities.map {it.authority}.toSet()
        if (authorities.contains("all:write")) {
            return true
        }
        return permissions.any { it in authorities }
    }

    fun hasAll(auth: Authentication, vararg permissions: String): Boolean {
        val authorities = auth.authorities.map {it.authority}.toSet()
        if (authorities.contains("all:write")) {
            return true
        }
        return permissions.all { it in authorities }
    }
}
