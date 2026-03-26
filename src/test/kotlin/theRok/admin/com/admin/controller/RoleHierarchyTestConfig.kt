package theRok.admin.com.admin.controller

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl

@TestConfiguration
class RoleHierarchyTestConfig {
    @Bean
    fun roleHierarchy(): RoleHierarchy =
        RoleHierarchyImpl.fromHierarchy("hello:write > hello:read")
}
