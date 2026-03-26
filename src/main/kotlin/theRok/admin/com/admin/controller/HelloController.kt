package theRok.admin.com.admin.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping("/hello")
class HelloController {
    @GetMapping
    @PreAuthorize("hasAuthority('hello:read')")
    fun hello(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        log.info { "authentication: $authentication" }
        return "Hello, World!"
    }

    /** 읽기 전용 토큰으로는 403 — `hello:write` 권한 필요 */
    @PostMapping("/demo-write")
    @PreAuthorize("hasAuthority('hello:write')")
    fun demoWrite(): String = "demo-write ok"
}
