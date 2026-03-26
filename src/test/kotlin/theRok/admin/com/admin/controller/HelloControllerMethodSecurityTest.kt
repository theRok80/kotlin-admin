package theRok.admin.com.admin.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import theRok.admin.com.admin.common.filter.MaintenanceFilter
import theRok.admin.com.admin.common.filter.RequestLoggingFilter
import theRok.admin.com.admin.common.interceptor.AuthIpThrottle
import theRok.admin.com.admin.config.MethodSecurityConfig
import theRok.admin.com.admin.exception.GlobalExceptionHandler

/**
 * JWT 없이 [@WithMockUser]로 권한 문자열만 주입해 [@PreAuthorize] 동작을 검증하는 예제.
 * RoleHierarchyTestConfig 에서 hello:write > hello:read 계층을 제공한다.
 */
@WebMvcTest(controllers = [HelloController::class])
@Import(MethodSecurityConfig::class, GlobalExceptionHandler::class, RoleHierarchyTestConfig::class)
@AutoConfigureMockMvc(addFilters = false)
class HelloControllerMethodSecurityTest {
    @MockBean
    private lateinit var maintenanceFilter: MaintenanceFilter

    @MockBean
    private lateinit var requestLoggingFilter: RequestLoggingFilter

    @MockBean
    private lateinit var authIpThrottle: AuthIpThrottle

    @MockBean
    private lateinit var messageSource: MessageSource

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser(authorities = ["hello:read"])
    fun `hello ok with read authority`() {
        mockMvc
            .perform(get("/hello"))
            .andExpect(status().isOk)
            .andExpect(content().string("Hello, World!"))
    }

    @Test
    @WithMockUser(authorities = ["hello:read"])
    fun `demo-write forbidden without write`() {
        mockMvc
            .perform(post("/hello/demo-write"))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(authorities = ["hello:read", "hello:write"])
    fun `demo-write ok with write`() {
        mockMvc
            .perform(post("/hello/demo-write"))
            .andExpect(status().isOk)
            .andExpect(content().string("demo-write ok"))
    }

    @Test
    @WithMockUser
    fun `hello forbidden when only default ROLE_USER`() {
        mockMvc
            .perform(get("/hello"))
            .andExpect(status().isForbidden)
    }

    // ── 계층 권한 테스트 ──────────────────────────────────────────────────
    @Test
    @WithMockUser(authorities = ["hello:write"])
    fun `hello ok with write only - hierarchy includes read`() {
        mockMvc
            .perform(get("/hello"))
            .andExpect(status().isOk)
            .andExpect(content().string("Hello, World!"))
    }

    @Test
    @WithMockUser(authorities = ["hello:write"])
    fun `demo-write ok with write only`() {
        mockMvc
            .perform(post("/hello/demo-write"))
            .andExpect(status().isOk)
            .andExpect(content().string("demo-write ok"))
    }
}
